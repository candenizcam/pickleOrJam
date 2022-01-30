package application

import application.puntainers.*
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
class TestScene(stage: PunStage) : PunScene(
    "testScene",
    stage,
    GlobalAccess.virtualSize.width.toDouble(),
    GlobalAccess.virtualSize.height.toDouble(),
    Colour.GRIZEL
) {
    private var clockStep = 0.0
    private var oscillator = 0.0
    private val hardwareClockPulseTime = 0.05
    val gameState = GameState(level= 0, money= 0, vinegar = 10, sugar = 10)

    override suspend fun sceneInit() {
        val a = WorkshopPuntainer.create(oneRectangle())
        addPuntainer(a)

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
                    toPuntainer("pauseMenuPuntainer") {
                        it.visible = true
                    }

                    toPuntainer("workshopPuntainer") {
                        it.visible = false
                    }
                    pauseGame(true)
                }
            })


        addPuntainer(PauseMenuPuntainer.create(oneRectangle()).also { puntainer ->
            puntainer.onReturn = {
                toPuntainer("workshopPuntainer") {
                    it.visible = true
                }
                puntainer.visible = false
                pauseGame(false)
            }
            puntainer.visible = false
        })

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


        musicPlayer.open("SlowDay.mp3", true)
        sfxPlayer.loadSounds(listOf("cash-register.mp3"))

        GlobalAccess.initLevels()
        val l = GlobalAccess.levels[gameState.level]

        openLevel(a, l)

        gameState.gameOver = {
            val levelEnd = LevelEndScene(stage)
            GlobalScope.launchImmediately {
                levelEnd.initialize()
                stage.scenesToAdd.add(Pair(levelEnd, true))
                stage.scenesToRemove.add("testScene")
            }
        }

        a.onChoice = { type, choice ->
            if (choice == 0 && gameState.vinegar > 0) {
                gameState.pickleIt(type)
            } else if (choice == 1 && gameState.sugar > 0) {
                gameState.jamIt(type)
            }
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
        active = !pause
    }


    private fun updateMoneyDisplay() {
        toPuntainer("moneyPuntainer", forceReshape = true) {
            it as SheetNumberDisplayer
            it.setValue(gameState.money)
        }
    }

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

        toPuntainer("clockPuntainer", forceReshape = true) {
            it as ClockPuntainer
            it.updateClockDisplayer()
        }


        toPuntainer("workshopPuntainer", forceReshape = true) {
            it as WorkshopPuntainer
            if (it.fruitPos.x !in -0.1..1.1) {
                it.deployNewFood()
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
        a.openLevel(l.fruitList.map { it.type })
        val timer = CountdownTimer(l.timeLimit)
        timer.onUpdate = {
            updateClock(timer.left)
        }
        timer.onComplete = {
            val levelEnd = LevelEndScene(stage)
            GlobalScope.launchImmediately {
                levelEnd.initialize()
                stage.scenesToAdd.add(Pair(levelEnd, true))
                stage.scenesToRemove.add("testScene")
            }
        }
        updatables.add(timer)
    }
}