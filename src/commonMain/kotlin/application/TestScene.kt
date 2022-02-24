package application

import application.puntainers.*
import com.pungo.admob.Admob
import com.soywiz.klock.TimeSpan
import com.soywiz.klock.blockingSleep
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korio.file.std.resourcesVfs
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import modules.basic.Colour
import pungine.PunScene
import pungine.PunStage
import pungine.geometry2D.Rectangle
import pungine.geometry2D.Vector
import pungine.geometry2D.oneRectangle
import pungine.uiElements.Button
import pungine.uiElements.PunImage

/** This scene is the template for a PunGineIV game
 *
 */

@OptIn(DelicateCoroutinesApi::class)
class TestScene(stage: PunStage, gameState: GameState = GameState(level= 0, money= 0, vinegar = 10, sugar = 10)) : PunScene(
    "testScene",
    stage,
    GlobalAccess.virtualSize.width.toDouble(),
    GlobalAccess.virtualSize.height.toDouble(),
    Colour.GRIZEL
) {
    private var clockStep = 0.0
    private var oscillator = 0.0
    private val hardwareClockPulseTime = 0.05
    var gameState = gameState


    override suspend fun sceneInit() {
        val a = WorkshopPuntainer.create(oneRectangle())
        addPuntainer(a)
        a.visible=active

        addPuntainer(
            Button(
                "pauseButton",
                GlobalAccess.virtualRect.toRated(
                    Rectangle(
                        Vector(16.0, 704.0),
                        68.0,
                        68.0,
                        Rectangle.Corners.TOP_LEFT
                    )
                ),
                resourcesVfs["UI/pause_normal.png"].readBitmap(),
                resourcesVfs["UI/pause_pushed.png"].readBitmap(),
                resourcesVfs["UI/pause_hover.png"].readBitmap()
            ).also { button ->
                button.clickFunction = {

                    pauseGame(true)
                }
            })


        addPuntainer(PauseMenuPuntainer.create(oneRectangle()).also { puntainer ->
            puntainer.onReturn = {
                puntainer.visible = false
                pauseGame(false)
                puntainer.resumeButtonVisible(true)
            }
            puntainer.visible = !active
            puntainer.onCreditsVisible = { b->
                toPuntainer("moneyPuntainer"){
                    it.visible = !b
                }
                toPuntainer("im"){
                    it.visible = !b
                }
                toPuntainer("clockPuntainer"){
                    it.visible = !b
                }
                toPuntainer("text"){
                    it.visible = !b
                }



            }
        })

        addPuntainer(
            PunImage("im",GlobalAccess.virtualRect.toRated(
                Rectangle(
                    Vector(874.0, 704.0),
                    196.0,
                    68.0,
                    cornerType = Rectangle.Corners.TOP_LEFT
                )
            ), resourcesVfs["UI/money.png"].readBitmap())
        )

        addPuntainer(
            SheetNumberDisplayer.create("moneyPuntainer",
                GlobalAccess.virtualRect.toRated(
                    Rectangle(
                        Vector(874.0, 704.0),
                        196.0,
                        68.0,
                        cornerType = Rectangle.Corners.TOP_LEFT
                    )
                ),
                Rectangle(0.0, 196.0, 0.0, 68.0),
                5,
                false,
                moneySign = true
            )
        )

        addPuntainer(
            ClockPuntainer.create(
                GlobalAccess.virtualRect.toRated(
                    Rectangle(
                        Vector(1076.0, 704.0),
                        196.0,
                        68.0,
                        cornerType = Rectangle.Corners.TOP_LEFT
                    )
                ),
                Rectangle(0.0, 196.0, 0.0, 68.0)
            )
        )

        addPuntainer(
            SheetLetterDisplayer.create("text",
                    GlobalAccess.rectFromXD(Vector(490.0,372.0),300,60),
                    Rectangle(0.0, 300.0, 0.0, 60.0),
                    16,
                    false,
                ).also {
                    it.setValue("HAPPY GAMING")
            }
        )







        a.onNewFruit = {
            setFruitText(it)
        }

        GlobalAccess.musicToggle = {
            if (it) {
                musicPlayer.play()
            } else {musicPlayer.pause()}
            sfxPlayer.soundOn = it
        }


        GlobalAccess.initLevels()
        val l = GlobalAccess.levels[gameState.level]

        openLevel(a, l)

        gameState.levelOver = {
            val levelEnd = LevelEndScene(stage, gameState)
            GlobalScope.launchImmediately {
                levelEnd.initialize()
                stage.scenesToAdd.add(Pair(levelEnd, true))
                stage.scenesToRemove.add("testScene")
            }
        }

        a.onChoice = { type, choice ->
            //coinVisible(true,false)
            if (choice == 0 && gameState.vinegar > 0) {
                var printableMoney = gameState.getFruit(type)?.jam ?: 0
                gameState.pickleIt(type)
                sfxPlayer.play("cash-register.mp3")
                a.coinVisible(if(printableMoney>50) 0 else 1)
                gameState.vinegar
            } else if (choice == 1 && gameState.sugar > 0) {
                var printableMoney = gameState.getFruit(type)?.jam ?: 0
                gameState.jamIt(type)
                sfxPlayer.play("cash-register.mp3")
                a.coinVisible(if(printableMoney<50) 0 else 1)
                gameState.sugar
            }else{
                -1
            }
        }

        a.updateSugarCount(gameState.sugar)
        a.updateVinCount(gameState.vinegar)

    }





    fun setFruitText(s: String){
        toPuntainer("text", forceReshape = true){ it as SheetLetterDisplayer
            it.setValue(s)
        }
    }

    fun generateLevel(level: Int) {
        val fruitList6 = mutableListOf<Fruit>()
        GlobalAccess.fullFlist.indices.forEach {
            fruitList6.add(Fruit(GlobalAccess.fullFlist[it], GlobalAccess.pFullList[it], 100- GlobalAccess.pFullList[it]))
        }
        GlobalAccess.levels.add(Level(fruitList6,30, GlobalAccess.levels.size*50+300))
    }

    private fun pauseGame(pause: Boolean) {

        toPuntainer("pauseMenuPuntainer") { it as PauseMenuPuntainer
            it.visible = pause
            it.resumeButtonVisible(true)
        }

        toPuntainer("workshopPuntainer") {
            it.visible = !pause
        }

        active = !pause
    }


    private fun updateMoneyDisplay() {
        toPuntainer("moneyPuntainer", forceReshape = true) {
            it as SheetNumberDisplayer
            it.setValue(gameState.money)
        }
    }

    var clockHolder = 0
    private fun updateClock(sec: Int) {
        toPuntainer("clockPuntainer", forceReshape = true) {
            it as ClockPuntainer
            it.setTimeAsSeconds(sec)
        }
    }


    override fun update(sec: Double) {
        super.update(sec)
        val newSec = oscillator + sec
        oscillator = if (newSec > hardwareClockPulseTime) {
            hardwareClockUpdateEmulator()
            newSec.rem(hardwareClockPulseTime)
        } else {
            newSec
        }
    }


    private fun hardwareClockUpdateEmulator() {
        clockStep += 1

        updateMoneyDisplay()
        updateClock(clockHolder)

        toPuntainer("clockPuntainer", forceReshape = true) {
            it as ClockPuntainer
            it.updateClockDisplayer()
        }


        toPuntainer("workshopPuntainer", forceReshape = true) {
            it as WorkshopPuntainer
            if (it.fruitPos.x !in -0.1..1.1) {
                it.deployNewFood()
                it.coinVisible(-1)

            } else {
                if (it.activeBasket!!.status == -1) {
                    it.discreteMove(x = 0, y = -1)
                } else {
                    if (it.activeBasket!!.status == 1) {
                        it.discreteMove(x = 1, y = 0)
                    } else {
                        it.discreteMove(x = -1, y = 0)
                    }
                    // conveyor moves by 30 pixels
                }
            }
        }
    }


    private suspend fun openLevel(a: WorkshopPuntainer, l: Level) {
        val adActions = Admob.Actions().also { actions ->
            actions.dismissAction = {
                println("DISMISSED!")
                GlobalScope.launchImmediately { a.openLevel(l.fruitList.map { it.type }) }
            }
            }
        (stage as TestStage).admob.interstitialPrepare(Admob.Config("TestAds", "ca-app-pub-3940256099942544/1033173712", actions = adActions))

        blockingSleep(TimeSpan(1000.0))

        //interstitialWaitAndShow(Admob.Config("TestAds", "ca-app-pub-3940256099942544/1033173712"))
        a.openLevel(l.fruitList.map { it.type })

        val timer = CountdownTimer(l.timeLimit)
        timer.onUpdate = {
            clockHolder = timer.left
        }
        timer.onComplete = {
            val levelEnd = LevelEndScene(stage, gameState)
            GlobalScope.launchImmediately {
                levelEnd.initialize()
                stage.scenesToAdd.add(Pair(levelEnd, true))
                stage.scenesToRemove.add("testScene")
            }
        }
        updatables.add(timer)
    }
}