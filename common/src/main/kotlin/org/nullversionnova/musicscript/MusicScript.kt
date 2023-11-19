package org.nullversionnova.musicscript

import dev.architectury.platform.Platform
import org.nullversionnova.musicscript.MusicScriptExpectPlatform.getConfigDirectory
import org.nullversionnova.musicscript.script.Events
import java.io.File
import java.util.Properties
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip

object MusicScript {
    const val MOD_ID = "musicscript"
    val DATA_PATH = "${Platform.getGameFolder()}/${MOD_ID}"
    val properties = Properties()

    fun init() {
        println("CONFIG DIR: ${getConfigDirectory().toAbsolutePath().normalize()}")
        val propertyFile = File("${Platform.getConfigFolder()}/$MOD_ID/musicscript.properties")
        if (!File(DATA_PATH).isDirectory) { File(DATA_PATH).mkdir() }
        if (propertyFile.exists()) {
            properties.load(propertyFile.inputStream())
        } else {
            setDefaultConfig()
        }
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

    fun setDefaultConfig() {
        properties["song_path"] = "$DATA_PATH/songs"
        properties["script_path"] = DATA_PATH
        properties["max_volume"] = -16
        properties["min_volume"] = -36
        properties["delay_entering_world"] = 200
        properties["delay_after_song"] = 2000
    }
}