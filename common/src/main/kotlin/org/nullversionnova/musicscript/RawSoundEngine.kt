package org.nullversionnova.musicscript

import net.minecraft.client.MinecraftClient
import net.minecraft.sound.SoundCategory
import java.io.File
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import javax.sound.sampled.FloatControl

object RawSoundEngine {
    private val loadedSounds = mutableMapOf<File,Clip>()
    private val gains = mutableMapOf<File,FloatControl>()
    private val MaxGain = -16
    private val MinGain = -36
    private var volume = (MinecraftClient.getInstance().options.getSoundVolume(SoundCategory.MUSIC) * MinecraftClient.getInstance().options.getSoundVolume(SoundCategory.MASTER))
    @JvmStatic
    fun start(file: File) : Boolean {
        if (volume <= 0) {
            return false
        }
        return if (file.exists()) {
            val audioIn = AudioSystem.getAudioInputStream(file.toURI().toURL())
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
        this.volume = volume
        for (i in gains.values) {
            i.value = volume * (MaxGain - MinGain) + MinGain
        }
    }
    @JvmStatic
    fun stop(sound: File) {
        loadedSounds[sound]?.close()
    }
    @JvmStatic
    fun stopAll() {
        for (i in loadedSounds.values) {
            i.stop()
        }
    }
    fun pause() {
        for (i in loadedSounds.values) {
            i.stop()
        }
    }
    fun resume() {
        for (i in loadedSounds.values) {
            i.start()
        }
    }
    fun isAnythingPlaying() : Boolean {
        for (i in loadedSounds.values) {
            if (i.isOpen) { return true }
        }
        return false
    }
}