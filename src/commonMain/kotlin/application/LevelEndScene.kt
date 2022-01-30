package application

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

        punImage("bg", oneRectangle(), resourcesVfs["workshop/Background.png"].readBitmap())


        /*
        Button("nextLevel", GlobalAccess.rectFromXD(Vector(488.0,328.0),304,80),
            resourcesVfs["workshop/play_next_normal.png"].readBitmap(),
            resourcesVfs["workshop/play_next_normal.png"].readBitmap(),
            resourcesVfs["workshop/play_next_hover.png"].readBitmap(),
        ).also {

        }

         */


        /**
         * top: 328px;
        left: 488px;
        width: 304px;
        height: 80px;
         */


    }
}