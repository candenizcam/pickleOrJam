package application.puntainers

import application.GlobalAccess
import com.soywiz.korge.input.onClick
import com.soywiz.korge.input.onUp
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import pungine.Puntainer
import pungine.geometry2D.Rectangle
import pungine.geometry2D.Vector
import pungine.geometry2D.oneRectangle
import pungine.uiElements.Button

class PauseMenuPuntainer private constructor(relativeRectangle: Rectangle): Puntainer("pauseMenuPuntainer",relativeRectangle) {

    var onCreditsVisible = {visible: Boolean->}

    private suspend fun init() {

        punImage("bg", oneRectangle(), resourcesVfs["workshop/background.png"].readBitmap())

        val thisRectangle = GlobalAccess.virtualRect.fromRated(relativeRectangle)



        val c =Button("credits",
            oneRectangle(),
            resourcesVfs["credits_bg.png"].readBitmap(),
            resourcesVfs["credits_bg.png"].readBitmap(),
            resourcesVfs["credits_bg.png"].readBitmap(),
            resourcesVfs["credits_bg.png"].readBitmap()
        )
        c.visible=false

        c.onClick {
            c.visible = false
            onCreditsVisible(false)
        }





        addPuntainer(
            Button("start", thisRectangle.toRated(Rectangle(Vector(520.0,720.0-104.0),240.0,80.0,Rectangle.Corners.TOP_LEFT)),
                resourcesVfs["UI/play_normal.png"].readBitmap(),
                resourcesVfs["UI/play_pushed.png"].readBitmap(),
                resourcesVfs["UI/play_hover.png"].readBitmap(),).also {
                it.clickFunction = {
                    // TODO start game
                    it.visible= true
                    onReturn()
                }
            }
        )

        addPuntainer(
            Button("resume", thisRectangle.toRated(Rectangle(Vector(520.0,720.0-104.0),240.0,80.0,Rectangle.Corners.TOP_LEFT)),
                resourcesVfs["UI/resume_normal.png"].readBitmap(),
                resourcesVfs["UI/resume_pushed.png"].readBitmap(),
                resourcesVfs["UI/resume_hover.png"].readBitmap(),).also {
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

                    GlobalAccess.musicToggle(false)
                }
            }
        )

        addPuntainer(
            Button("soundOff", thisRectangle.toRated(Rectangle(Vector(520.0,720.0-192.0),240.0,80.0,Rectangle.Corners.TOP_LEFT)),
                resourcesVfs["UI/no_sound_normal.png"].readBitmap(),
                resourcesVfs["UI/no_sound_pushed.png"].readBitmap(),
                resourcesVfs["UI/no_sound_hover.png"].readBitmap(),

                ).also {
                it.visible=false
                it.clickFunction = {
                    it.visible=false
                    puntainers.first{it.id == "soundOn"}.visible=true
                    GlobalAccess.musicToggle(true)
                }
            }
        )

        addPuntainer(
            Button("credits", thisRectangle.toRated(Rectangle(Vector(520.0,720.0-280.0),240.0,80.0,Rectangle.Corners.TOP_LEFT)),
                resourcesVfs["UI/credits_normal.png"].readBitmap(),
                resourcesVfs["UI/credits_pushed.png"].readBitmap(),
                resourcesVfs["UI/credits_hover.png"].readBitmap(),).also {
                it.clickFunction = {
                    c.visible=true
                    onCreditsVisible(true)
                    // TODO credits
                }
            }
        )


        addPuntainer(
            c
        )



    }

    var onReturn = {}
    fun resumeButtonVisible(boolean: Boolean=false){
        puntainers.first { it.id=="start" }.also { it as Button
            it.visible=false
            it.inactive = true
        }
        puntainers.first { it.id=="resume" }.also { it as Button
            it.visible=true
            it.inactive = false
        }
    }


    companion object {
        suspend fun create(relativeRectangle: Rectangle
        ): PauseMenuPuntainer {
            return PauseMenuPuntainer(relativeRectangle).also {
                it.init()
            }
        }
    }
}