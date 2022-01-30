package application

import modules.basic.Colour
import pungine.PunScene
import pungine.PunStage

class LevelEndScene(stage: PunStage) : PunScene("levelEnd", stage, GlobalAccess.virtualSize.width.toDouble(), GlobalAccess.virtualSize.height.toDouble(), Colour.MAGENTA) {
    override suspend fun sceneInit() {
        super.sceneInit()
        println("LEVEL OVER")
    }
}