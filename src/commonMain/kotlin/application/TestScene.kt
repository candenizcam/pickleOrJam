package application

import com.soywiz.klock.TimeSpan
import com.soywiz.korge.input.onClick
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korim.color.RGBA
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korma.geom.Angle
import modules.basic.Colour
import pungine.PunScene
import pungine.PunStage
import pungine.geometry2D.Rectangle
import pungine.geometry2D.oneRectangle

/** This scene is the template for a PunGineIV game
 *
 */

class TestScene(stage: PunStage): PunScene("testScene",stage,GlobalAccess.virtualSize.width.toDouble(),GlobalAccess.virtualSize.height.toDouble(), Colour.GRIZEL) {
    //override fun createSceneView(): Container = Puntainer()

    /*
    override suspend fun Container.sceneInit(): Unit{


        openingCrawl()






        super.sceneAfterInit()
    }

     */

    override suspend fun sceneInit() {
        openingCrawl()

    }

    override fun update(ms: Double) {

    }


    // delete from all under here for a new scene

    suspend fun openingCrawl(){

        val bg = solidRect("id", Rectangle(0.0,1.0,0.0,1.0),Colour.rgba(0.04,0.02,0.04,1.0))


        val img = punImage("id",Rectangle(390.0/scenePuntainer.width,890.0/scenePuntainer.width,110.0/scenePuntainer.height,610.0/scenePuntainer.height),resourcesVfs["pungo_transparent.png"].readBitmap()).also {
            it.visible=false
        }




        val resource = resourcesVfs["PunGine.png"].readBitmap()
        punImage("fader", oneRectangle(),bitmap = resource).also {
            it.alpha = 0.0
            var counter = 0.0
            it.addUpdater { dt: TimeSpan ->
                if(counter<3.0){
                    bg.alpha = 1.0
                    counter += dt.seconds
                    if(counter<1.2){
                        it.alpha = counter/1.2
                    }else if (counter>1.8){
                        it.alpha = (3.0-counter)/1.2
                    }
                }else{
                    it.alpha = 0.0
                    bg.alpha = 0.0
                    img.visible = true
                }
            }
        }

    }
}