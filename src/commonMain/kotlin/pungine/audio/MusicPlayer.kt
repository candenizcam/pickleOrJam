package pungine.audio

import com.soywiz.korau.sound.Sound
import com.soywiz.korau.sound.SoundChannel
import com.soywiz.korau.sound.readMusic
import com.soywiz.korio.file.std.resourcesVfs
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class MusicPlayer {
    private val channels = mutableListOf<SoundChannel>()
    private val active = mutableListOf<Boolean>()

    suspend fun open(list: List<String>, playNow: Boolean = true) {
        channels.clear()
        val files = mutableListOf<Sound>()
        list.forEach {
            files.add(resourcesVfs["music/$it"].readMusic())
            active.add(true)
        }
        files.forEach { channels.add(it.play()) }

        if (!playNow) channels.forEach { it.pause() }
    }

    fun toggleSilence(index: Int) {
        if(active[index])  {
            channels[index].volume = 0.0
            active[index] = false
        }
        else {
            var i = 0
            while(!active[i]) {
                i++
            }
            channels[index].volume = channels[i].volume
            active[index] = true
        }
    }

    suspend fun open(file: String, playNow: Boolean = true) {
        channels.clear()
        channels.add(resourcesVfs["music/$file"].readMusic().play())
        if (!playNow) channels.forEach { it.pause() }
    }

    fun release() {
        channels.forEach { it.stop() }
    }

    fun pause() {
        channels.forEach { it.pause() }
    }

    fun play() {
        channels.forEach { it.resume() }
    }
}
