package application.puntainers

import application.GlobalAccess
import com.soywiz.korge.input.onUp
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import pungine.Puntainer
import pungine.geometry2D.Rectangle
import pungine.geometry2D.Vector
import pungine.geometry2D.oneRectangle
import pungine.uiElements.Button

class PauseMenuPuntainer private constructor(relativeRectangle: Rectangle): Puntainer("pauseMenuPuntainer",relativeRectangle) {

    private suspend fun init() {

        punImage("bg", oneRectangle(), resourcesVfs["workshop/background.png"].readBitmap())

        val thisRectangle = GlobalAccess.virtualRect.fromRated(relativeRectangle)



        val start_button_on = resourcesVfs["game_logo.png"].readBitmap()


        addPuntainer(
            Button("start", thisRectangle.toRated(Rectangle(Vector(520.0,720.0-104.0),240.0,80.0,Rectangle.Corners.TOP_LEFT)),start_button_on).also {
                it.clickFunction = {
                    // TODO start game
                    onReturn()
                }
            }
        )

        addPuntainer(
            Button("resume", thisRectangle.toRated(Rectangle(Vector(520.0,720.0-104.0),240.0,80.0,Rectangle.Corners.TOP_LEFT)),start_button_on).also {
                it.visible = false
                it.clickFunction = {
                    onReturn()
                }
            }
        )

        addPuntainer(
            Button("soundOn", thisRectangle.toRated(Rectangle(Vector(520.0,720.0-192.0),240.0,80.0,Rectangle.Corners.TOP_LEFT)),
                resourcesVfs["UI/sound_on_normal.png"].readBitmap(),
                resourcesVfs["UI/sound_on_pushed.png"].readBitmap(),
                resourcesVfs["UI/sound_on_hover.png"].readBitmap(),

                ).also {
                it.clickFunction = {
                    it.visible=false
                    puntainers.first{it.id == "soundOff"}.visible=true
                    // TODO sound off
                }
            }
        )

        addPuntainer(
            Button("soundOff", thisRectangle.toRated(Rectangle(Vector(520.0,720.0-280.0),240.0,80.0,Rectangle.Corners.TOP_LEFT)),
                resourcesVfs["UI/no_sound_normal.png"].readBitmap(),
                resourcesVfs["UI/no_sound_pushed.png"].readBitmap(),
                resourcesVfs["UI/no_sound_hover.png"].readBitmap(),

                ).also {
                it.visible=false
                it.clickFunction = {
                    it.visible=false
                    puntainers.first{it.id == "soundOn"}.visible=true
                    // TODO sound on off
                    GlobalAccess.musicToggle()
                }
            }
        )

        addPuntainer(
            Button("credits", thisRectangle.toRated(Rectangle(Vector(566.0,412.0),148.0,44.0,Rectangle.Corners.TOP_LEFT)),start_button_on).also {
                it.clickFunction = {
                    // TODO credits
                }
            }
        )



    }

    var onReturn = {}

    companion object {
        suspend fun create(relativeRectangle: Rectangle
        ): PauseMenuPuntainer {
            return PauseMenuPuntainer(relativeRectangle).also {
                it.init()
            }
        }
    }
}