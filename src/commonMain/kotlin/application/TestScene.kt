package application

import com.soywiz.klock.TimeSpan
import com.soywiz.korge.view.addUpdater
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
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

class TestScene(stage: PunStage) : PunScene(
    "testScene",
    stage,
    GlobalAccess.virtualSize.width.toDouble(),
    GlobalAccess.virtualSize.height.toDouble(),
    Colour.GRIZEL
) {
    var vinegar = 0
    var sugar = 0

    var clockStep = 0.0
    var oscillator = 0.0
    val hardwareClockPulseTime = 0.05
    var timeLimit = 0

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
            ).also {
                it.clickFunction = {
                    // todo pause button
                    toPuntainer("pauseMenuPuntainer") {
                        it.visible = true
                    }

                    toPuntainer("workshopPuntainer") {
                        it.visible = false
                    }
                    pauseGame(true)
                }
            })


        addPuntainer(PauseMenuPuntainer.create(oneRectangle()).also {
            it.onReturn = {
                toPuntainer("workshopPuntainer") {
                    it.visible = true
                }
                it.visible = false
                pauseGame(false)
            }
            it.visible = false
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
        GlobalAccess.initInputs()
        val l = Level(GlobalAccess.inputList, 5)

        openLevel(a, l)

        a.onChoice = { type, choice ->
            if (choice == 0 && GlobalAccess.gameState.vinegar > 0) {
                GlobalAccess.gameState.pickleIt(type)
            } else if (choice == 1 && GlobalAccess.gameState.sugar > 0) {
                GlobalAccess.gameState.jamIt(type)
            }
        }
    }


    fun pauseGame(pause: Boolean) {
        active = !pause
    }


    fun updateMoneyDisplay(value: Int) {
        toPuntainer("moneyPuntainer", forceReshape = true) {
            it as MoneyPuntainer
            it.setMoney(GlobalAccess.gameState.money)
        }
    }

    fun updateClock(sec: Int) {
        toPuntainer("clockPuntainer", forceReshape = true) {
            it as ClockPuntainer
            it.setTimeAsSeconds(sec)
        }
    }

    override fun update(ms: Double) {
        super.update(ms)
        vinegar = GlobalAccess.gameState.vinegar
        sugar = GlobalAccess.gameState.sugar

        val newSec = oscillator + ms
        oscillator = if (newSec > hardwareClockPulseTime) {
            hardwareClockUpdateEmulator()
            newSec.rem(hardwareClockPulseTime)
        } else {
            newSec
        }
    }

    fun hardwareClockUpdateEmulator() {
        clockStep += 1
        val sec = clockStep * hardwareClockPulseTime
      /*  if ((sec + 0.005).rem(0.5) < 0.01) {
            updateClock(sec.toInt())
        }

       */

        updateMoneyDisplay(sec.toInt())
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


    suspend fun openLevel(a: WorkshopPuntainer, l: Level) {
        a.openLevel(l.fruitList.map { it.type })
        val timer = CountdownTimer(l.timeLimit)
        timer.onUpdate = {
            updateClock(timer.left) }
        timer.onComplete = {
            stage.scenesToAdd.add(Pair(LevelEndScene(stage), true))
            stage.scenesToRemove.add("testScene")
        }
        updatables.add(timer)
    }


    // delete from all under here for a new scene

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
}