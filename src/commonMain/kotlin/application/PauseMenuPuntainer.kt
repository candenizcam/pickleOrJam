package application

import com.soywiz.korge.input.onUp
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import modules.basic.Colour
import pungine.Puntainer
import pungine.geometry2D.Rectangle
import pungine.geometry2D.oneRectangle

class PauseMenuPuntainer private constructor(relativeRectangle: Rectangle): Puntainer("pauseMenuPuntainer",relativeRectangle) {

    private suspend fun init() {
        solidRect("bg", oneRectangle(), colour = Colour.GRIZEL).also {
            it.onUp {
                onReturn()
            }
        }


        resourcesVfs["grid44.png"].readBitmap().also {
            punImage("menuImage", Rectangle(0.2,0.8,0.2,0.8), bitmap = it)
        }

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