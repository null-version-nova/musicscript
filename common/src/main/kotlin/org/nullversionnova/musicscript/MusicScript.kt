package org.nullversionnova.musicscript

import dev.architectury.event.events.client.ClientTickEvent
import dev.architectury.event.events.common.TickEvent
import dev.architectury.platform.Platform
import net.minecraft.client.Minecraft
import net.minecraft.client.Options
import net.minecraft.network.chat.Component.Serializer.fromJson
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.player.Player
import org.nullversionnova.musicscript.MusicScriptExpectPlatform.getConfigDirectory
import java.io.File
import java.io.FileReader
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import javax.sound.sampled.Control
import javax.sound.sampled.FloatControl


object MusicScript {
    const val MOD_ID = "musicscript"
    val DATA_PATH = "${Platform.getConfigFolder()}/${MOD_ID}"
    fun init() {
        println("CONFIG DIR: ${getConfigDirectory().toAbsolutePath().normalize()}")
        val clip = playSound("song.wav")
        val gain = clip.getControl(FloatControl.Type.MASTER_GAIN) as FloatControl
        ClientTickEvent.Client {
            gain.value = (it.options.getSoundSourceVolume(SoundSource.MUSIC) * 30f) - 30f
            println(it.options.getSoundSourceVolume(SoundSource.MUSIC))
        }
    }



    fun ripMinecraftSongs() {
        val OBJECT_INDEX = fromJson(FileReader("${Platform.getGameFolder()}/assets/indexes/${Platform.getMinecraftVersion()}.json").toString())
        val OBJECT_PATH = "${Platform.getGameFolder()}/assets/objects"
        val OUTPUT_PATH = "${DATA_PATH}/music"
    }

    fun findAssetsFolder() {
    }

    fun playSound(soundFile: String) : Clip {
        val f = File(soundFile)
        val audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL())
        val clip = AudioSystem.getClip()
        clip.open(audioIn)
        val gain = clip.getControl(FloatControl.Type.MASTER_GAIN) as FloatControl
        gain.value = -30f
        println(Minecraft.getInstance().options.getSoundSourceVolume(SoundSource.MUSIC))
        clip.start()
        return clip
    }
}