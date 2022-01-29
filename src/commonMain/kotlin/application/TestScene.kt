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

    var sec = 0.0
    override suspend fun sceneInit() {
        //openingCrawl()
        val a = WorkshopPuntainer.create(oneRectangle())
        addPuntainer(a)


        addPuntainer(Button("pauseButton", GlobalAccess.virtualRect.toRated( Rectangle(Vector(16.0,704.0),68.0,68.0,Rectangle.Corners.TOP_LEFT)), resourcesVfs["buttons/pause_button.png"].readBitmap()).also {
            it.clickFunction = {
                // todo pause button
                toPuntainer("pauseMenuPuntainer"){
                    it.visible = true
                }

                toPuntainer("workshopPuntainer"){
                    it.visible=false
                }
                pauseGame(true)
            }
        })


        addPuntainer(PauseMenuPuntainer.create(oneRectangle()).also {
            it.onReturn = {
                toPuntainer("workshopPuntainer"){
                    it.visible = true
                }
                it.visible=false
                pauseGame(false)
            }
            it.visible=false
        })

        /*
        val layers = mutableListOf<String>()
        layers.add("aitu1.mp3")
        layers.add("aitu2.mp3")
        layers.add("aitu3.mp3")
        musicPlayer.open(layers, true)
         */
        GlobalAccess.initInputs()

        openLevel(a)

        a.onChoice = { type, choice ->
            if (choice == 0 && GlobalAccess.gameState.vinegar>0) {
                GlobalAccess.gameState.pickleIt(type)
            } else if (choice == 1 && GlobalAccess.gameState.sugar>0) {
                GlobalAccess.gameState.jamIt(type)
            }
        }
    }


    fun pauseGame(pause: Boolean){

    }

    override fun update(ms: Double) {
        vinegar = GlobalAccess.gameState.vinegar
        sugar = GlobalAccess.gameState.sugar
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
                            it.conveyorPos.x + ms * 0.5
                    }else{
                            it.conveyorPos.x - ms * 0.5
                    }
                    it.moveOnConveyor(setX = newPosX)
                }
            }
            val newSec= sec + ms
            if(newSec.toInt()!=sec.toInt()){
                it.updateClockBySec(newSec.toInt())

            }
            sec = newSec

        }
    }



    suspend fun openLevel(a: WorkshopPuntainer) {
        a.openLevel(GlobalAccess.inputList.map {it.type})
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