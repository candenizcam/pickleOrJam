package application

import com.soywiz.klock.TimeSpan
import com.soywiz.korge.view.addUpdater
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import modules.basic.Colour
import pungine.PunScene
import pungine.PunStage
import pungine.geometry2D.Rectangle
import pungine.geometry2D.oneRectangle

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

    override suspend fun sceneInit() {
        //openingCrawl()
        val a = WorkshopPuntainer.create(oneRectangle())
        addPuntainer(a)

        openLevel(a)
        /*
        val layers = mutableListOf<String>()
        layers.add("aitu1.mp3")
        layers.add("aitu2.mp3")
        layers.add("aitu3.mp3")
        musicPlayer.open(layers, true)
         */
        GlobalAccess.initInputs()

        a.onChoice = { type, choice ->
            if (choice == 0) {
                GlobalAccess.gameState.pickleIt(type)
            } else if (choice == 1) {
                GlobalAccess.gameState.jamIt(type)
            }
        }
    }

    override fun update(ms: Double) {
        toPuntainer("workshopPuntainer", forceReshape = true){ it as WorkshopPuntainer
            if(it.conveyorPos.x !in -0.1..1.1){
                it.deployNewFood()
            }else{
                if(it.activeBasket!!.status==-1){
                    var newPosY = it.conveyorPos.y-ms*0.5
                    if(newPosY<=0.5){
                        newPosY = 0.5
                    }
                    it.moveOnConveyor(setY = newPosY)
                }else{
                    val newPosX = if(it.activeBasket!!.status==1){
                        it.conveyorPos.x+ms*0.5
                    }else{
                        it.conveyorPos.x-ms*0.5
                    }
                    it.moveOnConveyor(setX = newPosX)
                }




            }
        }
    }


    suspend fun openLevel(a: WorkshopPuntainer) {
        a.openLevel(listOf("apple", "apple", "orange", "cucumber", "eggplant", "cucumber"))

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