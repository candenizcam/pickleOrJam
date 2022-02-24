package application

import com.soywiz.korge.component.docking.dockedTo
import com.soywiz.korge.component.onStageResized
import com.soywiz.korge.view.*
import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korio.file.std.resourcesVfs
import modules.basic.Colour
import pungine.*
import pungine.uiElements.PunImage
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

/** This scene is the template for a PunGineIV game
 *
 */

class TestStage: PunStage() {
    var storyScene = StoryScene(this)

    @OptIn(ExperimentalTime::class)
    override suspend fun Container.sceneMain(){
        storyScene.active=false
        storyScene.initialize()
        scenesToAdd.add(Pair(storyScene,false))

        musicPlayer.open("SlowDay.mp3", true)
        sfxPlayer.loadSounds(listOf("cash-register.mp3"))


    }
}