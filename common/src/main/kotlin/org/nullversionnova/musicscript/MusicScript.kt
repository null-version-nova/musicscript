package org.nullversionnova.musicscript

import dev.architectury.platform.Platform
import net.minecraft.network.chat.Component.Serializer.fromJson
import org.nullversionnova.musicscript.MusicScriptExpectPlatform.getConfigDirectory
import java.io.File
import java.io.FileReader
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip


object MusicScript {
    const val MOD_ID = "musicscript"
    val DATA_PATH = "${Platform.getConfigFolder()}/${MOD_ID}"
    fun init() {
        println("CONFIG DIR: ${getConfigDirectory().toAbsolutePath().normalize()}")
        SoundManager.start("otherside.wav")
    }



    fun ripMinecraftSongs() {
        val OBJECT_INDEX = fromJson(FileReader("${Platform.getGameFolder()}/assets/indexes/${Platform.getMinecraftVersion()}.json").toString())
        val OBJECT_PATH = "${Platform.getGameFolder()}/assets/objects"
        val OUTPUT_PATH = "${DATA_PATH}/music"
    }

    fun findAssetsFolder() {
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