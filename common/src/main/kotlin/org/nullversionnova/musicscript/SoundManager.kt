package org.nullversionnova.musicscript

import net.minecraft.client.Minecraft
import net.minecraft.sounds.SoundSource
import java.io.File
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import javax.sound.sampled.FloatControl

object SoundManager {
    private val loadedSounds = mutableMapOf<String,Clip>()
    private val gains = mutableMapOf<String,FloatControl>()
    private val MaxGain = 6
    private val MinGain = -36
    private var volume = (Minecraft.getInstance().options.getSoundSourceVolume(SoundSource.MUSIC) * Minecraft.getInstance().options.getSoundSourceVolume(SoundSource.MASTER))
    @JvmStatic
    fun start(file: String) : Boolean {
        if (volume <= 0) {
            return false
        }
        val f = File(file)
        return if (f.exists()) {
            val audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL())
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
    fun stop(sound: String) {
        loadedSounds[sound]?.close()
    }
    @JvmStatic
    fun stopAll() {
        for (i in loadedSounds.values) {
            i.stop()
        }
    }
}