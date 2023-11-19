package org.nullversionnova.musicscript.sound

import net.minecraft.client.MinecraftClient
import net.minecraft.sound.SoundCategory
import java.io.File
import java.io.InputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import javax.sound.sampled.FloatControl

object RawSoundEngine {
    private val loadedSounds = mutableMapOf<File,Clip>()
    private val gains = mutableMapOf<File,FloatControl>()
    private val MaxGain = -16
    private val MinGain = -36
    private var volume = (MinecraftClient.getInstance().options.getSoundVolume(SoundCategory.MUSIC) * MinecraftClient.getInstance().options.getSoundVolume(SoundCategory.MASTER))
    private var paused = false
    @JvmStatic
    fun start(file: File, stream: InputStream = file.inputStream()) : Boolean {
        if (volume <= 0) {
            return false
        }

        return if (file.exists()) {
            val audioIn = AudioSystem.getAudioInputStream(stream)
            val clip = AudioSystem.getClip()
            clip.open(audioIn)
            loadedSounds[file] = clip
            gains[file] = clip.getControl(FloatControl.Type.MASTER_GAIN) as FloatControl
            gains[file]!!.value = volume * (MaxGain - MinGain) + MinGain
            clip.start()
            true
        } else {
            println("$file not found")
            false
        }
    }
    @JvmStatic
    fun setVolume(volume: Float) {
        RawSoundEngine.volume = volume
        for (i in gains.values) {
            i.value = volume * (MaxGain - MinGain) + MinGain
        }
    }
    @JvmStatic
    fun stopSound(sound: File) {
        loadedSounds[sound]?.close()
        loadedSounds.remove(sound)
    }
    @JvmStatic
    fun stopSounds() {
        for (i in loadedSounds.keys) {
            stopSound(i)
        }
    }
    fun pause() {
        for (i in loadedSounds.values) {
            if (i.isRunning) { paused = true }
            i.stop()
        }
    }
    fun resume() {
        paused = false
        for (i in loadedSounds.values) {
            i.start()
        }
    }
    fun isAnythingPlaying() : Boolean {
        for (i in loadedSounds.values) {
            if (i.isRunning) { return true }
        }
        return paused
    }
}