package application


import com.pungo.admob.Admob
import com.pungo.admob.AdmobCreate
import com.soywiz.korge.component.docking.dockedTo
import com.soywiz.korge.component.onStageResized
import com.soywiz.korge.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import pungine.*
import kotlin.time.ExperimentalTime
/** This scene is the template for a PunGineIV game
 *
 */

class TestStage: PunStage() {
    lateinit var admob: Admob
    var storyScene = StoryScene(this)

    @OptIn(ExperimentalTime::class)
    override suspend fun Container.sceneMain(){
        admob = AdmobCreate(this@TestStage.views, testing=true)


        storyScene.active=false
        storyScene.initialize()
        scenesToAdd.add(Pair(storyScene,false))

        musicPlayer.open("SlowDay.mp3", true)
        sfxPlayer.loadSounds(listOf("cash-register.mp3"))

        GlobalScope.async {
            GlobalAccess.commonAssets.load()
        }

    }
}