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
    suspend fun loadSounds(list: List<String>) {
        list.forEach { sfxList.add(Pair(it, resourcesVfs["sfx/$it"].readSound())) }
        /*
        GlobalScope.launchImmediately {
            resourcesVfs["sfx"].list().collect { file -> sfxList.add(Pair(file.baseName, file.readSound())) }
        }
         */
    }

    fun play(name: String) {
        GlobalScope.launchImmediately{
           // resourcesVfs["sfx/$name"].readSound().play()
            sfxList.find{pair -> pair.first == name}?.second?.play()
        }
    }
}