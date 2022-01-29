package application

import com.soywiz.korge.input.onUp
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import modules.basic.Colour
import pungine.Puntainer
import pungine.geometry2D.Rectangle
import pungine.geometry2D.Vector
import pungine.geometry2D.oneRectangle
import pungine.uiElements.Button

class PauseMenuPuntainer private constructor(relativeRectangle: Rectangle): Puntainer("pauseMenuPuntainer",relativeRectangle) {

    private suspend fun init() {

        punImage("bg", oneRectangle(), resourcesVfs["workshop/background.png"].readBitmap())

        val thisRectangle = GlobalAccess.virtualRect.fromRated(relativeRectangle)


        resourcesVfs["game_logo.png"].readBitmap().also {
            punImage("menuImage", thisRectangle.toRated(Rectangle(Vector(524.0,566.0),232.0,240.0,Rectangle.Corners.TOP_LEFT)), bitmap = it).also {
                it.onUp {

                }
            }
        }

        val start_button_on = resourcesVfs["game_logo.png"].readBitmap()


        addPuntainer(
            Button("start", thisRectangle.toRated(Rectangle(Vector(566.0,524.0),148.0,44.0,Rectangle.Corners.TOP_LEFT)),start_button_on).also {
                it.clickFunction = {
                    // TODO start game
                    onReturn()
                }
            }
        )

        addPuntainer(
            Button("resume", thisRectangle.toRated(Rectangle(Vector(566.0,524.0),148.0,44.0,Rectangle.Corners.TOP_LEFT)),start_button_on).also {
                it.visible = false
                it.clickFunction = {
                    onReturn()
                }
            }
        )

        addPuntainer(
            Button("sound", thisRectangle.toRated(Rectangle(Vector(566.0,468.0),148.0,44.0,Rectangle.Corners.TOP_LEFT)),start_button_on).also {
                it.clickFunction = {
                    // TODO sound on off
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