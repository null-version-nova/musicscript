package org.nullversionnova.musicscript

import dev.architectury.platform.Platform
import org.nullversionnova.musicscript.MusicScriptExpectPlatform.getConfigDirectory
import org.python.core.PyDictionary
import java.io.File
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip

object MusicScript {
    const val MOD_ID = "musicscript"
    val DATA_PATH = "${Platform.getConfigFolder()}/${MOD_ID}"
    val MUSIC_PATH = "$DATA_PATH/songs"
    fun init() {
        println("CONFIG DIR: ${getConfigDirectory().toAbsolutePath().normalize()}")
        Events.init()
    }

    fun playSound(soundFile: String) : Clip? {
        val f = File(soundFile)
        return if (f.exists()) {
            val audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL())
            val clip = AudioSystem.getClip()
            clip.open(audioIn)
            clip.start()
            clip
        } else {
            println("$soundFile not found")
            null
        }
    }
}