package application

import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korio.file.std.resourcesVfs
import kotlinx.coroutines.DelicateCoroutinesApi
import modules.basic.Colour
import pungine.PunScene
import pungine.PunStage
import pungine.geometry2D.Rectangle
import pungine.geometry2D.Vector
import pungine.geometry2D.oneRectangle
import pungine.uiElements.Button

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
                resourcesVfs["buttons/pause_button.png"].readBitmap()
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
                        Vector(1116.0, 704.0),
                        148.0,
                        68.0,
                        cornerType = Rectangle.Corners.TOP_LEFT
                    )
                ),
                Rectangle(0.0, 148.0, 0.0, 68.0)
            )
        )


        addPuntainer(
            MoneyPuntainer.create(
                GlobalAccess.virtualRect.toRated(
                    Rectangle(
                        Vector(952.0, 704.0),
                        148.0,
                        68.0,
                        cornerType = Rectangle.Corners.TOP_LEFT
                    )
                ),
                Rectangle(0.0, 148.0, 0.0, 68.0)
            )
        )

        musicPlayer.open("SlowDay.mp3", true)
        GlobalAccess.initLevels()
        val l = Level(GlobalAccess.levels[GlobalAccess.gameState.level].fruitList, 120)

        openLevel(a, l)

        a.onChoice = { type, choice ->
            if (choice == 0 && GlobalAccess.gameState.vinegar > 0) {
                GlobalAccess.gameState.pickleIt(type)
            } else if (choice == 1 && GlobalAccess.gameState.sugar > 0) {
                GlobalAccess.gameState.jamIt(type)
            }
        }
    }


    private fun pauseGame(pause: Boolean) {
        active = !pause
    }


    private fun updateMoneyDisplay() {
        toPuntainer("moneyPuntainer", forceReshape = true) {
            it as MoneyPuntainer
            it.setMoney(GlobalAccess.gameState.money)
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
            updateClock(timer.left) }
        timer.onComplete = {
            val les = LevelEndScene(stage)
            stage.scenesToAdd.add(Pair(les, true))
            stage.scenesToRemove.add("testScene")
        }
        updatables.add(timer)
    }


    // delete from all under here for a new scene

    /*
    suspend fun openingCrawl() {

        val bg = solidRect("id", Rectangle(0.0, 1.0, 0.0, 1.0), Colour.rgba(0.04, 0.02, 0.04, 1.0))


        val img = punImage(
            "id",
            Rectangle(
                390.0 / scenePuntainer.width,
                890.0 / scenePuntainer.width,
                110.0 / scenePuntainer.height,
                610.0 / scenePuntainer.height
            ),
            resourcesVfs["pungo_transparent.png"].readBitmap()
        ).also {
            it.visible = false
        }

        val resource = resourcesVfs["PunGine.png"].readBitmap()
        punImage("fader", oneRectangle(), bitmap = resource).also {
            it.alpha = 0.0
            var counter = 0.0
            it.addUpdater { dt: TimeSpan ->
                if (counter < 3.0) {
                    bg.alpha = 1.0
                    counter += dt.seconds
                    if (counter < 1.2) {
                        it.alpha = counter / 1.2
                    } else if (counter > 1.8) {
                        it.alpha = (3.0 - counter) / 1.2
                    }
                } else {
                    it.alpha = 0.0
                    bg.alpha = 0.0
                    img.visible = true
                }
            }
        }

    }
     */
}