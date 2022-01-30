package application

import application.puntainers.MoneyPuntainer
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import modules.basic.Colour
import pungine.PunScene
import pungine.PunStage
import pungine.geometry2D.Rectangle
import pungine.geometry2D.Vector
import pungine.geometry2D.oneRectangle
import pungine.uiElements.Button

class LevelEndScene(stage: PunStage) : PunScene("levelEnd", stage, GlobalAccess.virtualSize.width.toDouble(), GlobalAccess.virtualSize.height.toDouble()) {
    override suspend fun sceneInit() {
        super.sceneInit()

        punImage("bg", oneRectangle(), resourcesVfs["UI/between.png"].readBitmap())



        Button("nextLevel", GlobalAccess.rectFromXD(Vector(488.0,328.0),304,80),
            resourcesVfs["UI/play_next_normal.png"].readBitmap(),
            resourcesVfs["UI/play_next_pushed.png"].readBitmap(),
            resourcesVfs["UI/play_next_hover.png"].readBitmap(),
        ).also {
            addPuntainer(it)
            it.clickFunction = {
                onPlayNextPressed()
            }

            addPuntainer(
                MoneyPuntainer.create(
                    GlobalAccess.virtualRect.toRated(
                        GlobalAccess.rectFromXD(Vector(872.0,308.0),192,36)
                    ),
                    Rectangle(0.0, 192.0, 0.0, 36.0)
                )
            )
        }




        /**
        top: 308px;
        left: 872px;
        width: 192px;
        height: 36px;
         */


    }

    fun onPlayNextPressed(){

    }
}