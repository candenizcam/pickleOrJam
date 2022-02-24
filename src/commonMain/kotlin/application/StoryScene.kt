package application

import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korio.file.std.resourcesVfs
import kotlinx.coroutines.GlobalScope
import pungine.PunScene
import pungine.PunStage
import pungine.geometry2D.oneRectangle
import pungine.uiElements.Button

class StoryScene(stage: PunStage) : PunScene("storyScene", stage, GlobalAccess.virtualSize.width.toDouble(), GlobalAccess.virtualSize.height.toDouble()) {
    override suspend fun sceneInit() {



        val b1 = Button(upBitmap =  resourcesVfs["story/Story_1.png"].readBitmap(), downRate = 1.0, hoverOffset = 0)
        val b2 = Button(upBitmap =  resourcesVfs["story/Story_2.png"].readBitmap(), downRate = 1.0, hoverOffset = 0)

        b1.clickFunction = {
            b1.visible=false
            b2.visible=true
        }

        b2.visible = false

        b2.clickFunction = {
            val newGame = TestScene(stage)
            newGame.active=false
            GlobalScope.launchImmediately {
                newGame.initialize()

                stage.scenesToAdd.add(Pair(newGame, false))
                stage.scenesToRemove.add("storyScene")
            }
        }

        addPuntainer(b1)
        addPuntainer(b2)

    }

}