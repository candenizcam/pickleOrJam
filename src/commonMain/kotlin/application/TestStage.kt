package application


import com.pungo.admob.Admob
import com.pungo.admob.AdmobCreate
import com.soywiz.korge.view.*
import pungine.*
import kotlin.time.ExperimentalTime
/** This scene is the template for a PunGineIV game
 *
 */

class TestStage: PunStage() {
    var testScene = TestScene(this)
    lateinit var admob: Admob

    @OptIn(ExperimentalTime::class)
    override suspend fun Container.sceneMain(){
        admob = AdmobCreate(this@TestStage.views, testing=true)
        testScene.active=false
        testScene.initialize()
        scenesToAdd.add(Pair(testScene,false))

        musicPlayer.open("SlowDay.mp3", true)
        sfxPlayer.loadSounds(listOf("cash-register.mp3"))
    }
}