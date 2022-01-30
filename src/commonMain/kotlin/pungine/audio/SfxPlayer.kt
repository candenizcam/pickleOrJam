package pungine.audio

import com.soywiz.korau.sound.Sound
import com.soywiz.korau.sound.readSound
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korio.file.baseName
import com.soywiz.korio.file.std.resourcesVfs
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect

@OptIn(InternalCoroutinesApi::class, DelicateCoroutinesApi::class)
class SfxPlayer {
    private val sfxList = mutableListOf<Pair<String, Sound>>()
    suspend fun loadSounds() {
        GlobalScope.launchImmediately {
            resourcesVfs["sfx"].list().collect { file -> sfxList.add(Pair(file.baseName, file.readSound())) }
        }
    }

    fun play(name: String) {
        GlobalScope.launchImmediately{
            sfxList.find{pair -> pair.first == name}?.second?.play()
        }
    }
}