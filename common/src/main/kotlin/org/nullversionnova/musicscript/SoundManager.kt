package org.nullversionnova.musicscript

import java.io.File
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import javax.sound.sampled.FloatControl

object SoundManager {
    private val loadedSounds = mutableMapOf<String,Clip>()
    private val gains = mutableMapOf<String,FloatControl>()
    fun loadSound(file: String) : Boolean {
        val f = File(file)
        return if (f.exists()) {
            val audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL())
            val clip = AudioSystem.getClip()
            clip.open(audioIn)
            loadedSounds[file] = clip
            gains[file] = clip.getControl(FloatControl.Type.MASTER_GAIN) as FloatControl
            clip.start()
            true
        } else {
            println("$file not found")
            false
        }
    }
    @JvmStatic
    fun setGain(sound: String, gain: Float) {
        gains[sound]?.value = gain
    }
    fun stop(sound: String) {
        loadedSounds[sound]?.stop()
    }
}