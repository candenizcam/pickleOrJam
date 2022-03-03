package pungine

import application.GlobalAccess
import com.soywiz.klock.TimeSpan
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.*
import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.color.Colors
import com.soywiz.korim.color.RGBA
import com.soywiz.korio.async.launchImmediately
import pungine.audio.MusicPlayer
import pungine.audio.SfxPlayer

import pungine.geometry2D.Rectangle
import pungine.geometry2D.Vector
import pungine.geometry2D.oneRectangle
import kotlin.reflect.KClass

open class PunStage(var width: Double = InternalGlobalAccess.virtualSize.width.toDouble(), var height: Double=InternalGlobalAccess.virtualSize.height.toDouble()): Scene() {
    override fun createSceneView(): Puntainer = Puntainer().also {
        scenePuntainer = it
    }
    lateinit var scenePuntainer: Puntainer // this can be referred as the scene puntainer directly

    val centre: Vector
        get() {
            return Vector(width*0.5,height*0.5)
        }


    val thisRectangle: Rectangle
        get() {
            return InternalGlobalAccess.virtualRect
        }

    val virtualWidth: Double
        get() {
            return this.thisRectangle.width
        }

    val virtualHeight: Double
        get() {
            return  this.thisRectangle.height
        }

    val musicPlayer = MusicPlayer()
    val sfxPlayer = SfxPlayer()
    val scenes = mutableListOf<PunScene>()
    val scenesToAdd = mutableListOf<Pair<PunScene, Boolean>>()
    val scenesToRemove = mutableListOf<String>()

    override suspend fun Container.sceneInit() {
        addUpdater { dt->
            update(dt)
        }
    }
    fun add(scene: PunScene, active: Boolean) {
        if (scenes.any { it.id == scene.id }) {
            throw Exception("A scene with this id already exists: " + scene.id)
        } else {
            scene.active = active
            scenes.add(scene)
            sceneContainer.addChild(scene.scenePuntainer)
        }
    }

    fun update(dt: TimeSpan) {

        scenes.forEach {
            if (it.active) it.update(dt.seconds)
        }

        try {
            scenesToRemove.forEach {
                val s = findScene(it)
                scenes.remove(s)
                sceneContainer.removeChild(s.scenePuntainer)
            }
            val staPair = scenesToAdd.partition { it.first.initializationComplete }
            scenesToAdd.clear()
            scenesToAdd.addAll(staPair.second)
            staPair.first.forEach {
                scenePuntainer.addChild(it.first.scenePuntainer)
                add(it.first, it.second)
            }
        }catch (e: Exception){
            println("Error in scene change ${e.message}")
        } finally {
            scenesToRemove.clear()
        }
    }

    fun findScene(id: String): PunScene {
        val found = scenes.filter { it.id == id }
        when {
            found.isEmpty() -> {
                throw Exception("No scene with this name: $id")
            }
            found.size == 1 -> {
                return found[0]
            }
            else -> {
                throw Exception("There are multiple scenes by this name: $id")
            }
        }
    }
}