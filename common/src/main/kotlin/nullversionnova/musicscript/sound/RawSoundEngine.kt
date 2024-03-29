package nullversionnova.musicscript.sound

import net.minecraft.client.MinecraftClient
import net.minecraft.sound.SoundCategory
import nullversionnova.musicscript.script.Song
import java.io.File
import java.io.InputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import javax.sound.sampled.FloatControl

class RawSoundEngine {
    val loadedSounds = mutableMapOf<File,Clip>()
    private val gains = mutableMapOf<File,FloatControl>()
    private val MaxGain = -16
    private val MinGain = -36
    private var volume = (MinecraftClient.getInstance().options.getSoundVolume(SoundCategory.MUSIC) * MinecraftClient.getInstance().options.getSoundVolume(SoundCategory.MASTER))
    private var paused = false
    fun start(file: File, stream: InputStream = file.inputStream(), songVolume: Float = 1.0f, loop : Boolean = false, start: () -> Long = {0}) : Boolean {
        if (volume <= 0) {
            return false
        }

        return if (file.exists()) {
            val audioIn = AudioSystem.getAudioInputStream(stream)
            val clip = AudioSystem.getClip()
            clip.open(audioIn)
            if (loop) {
                clip.loop(-1)
                clip.setLoopPoints(0,-1)
            }
            loadedSounds[file]?.stop()
            loadedSounds[file] = clip
            gains[file] = clip.getControl(FloatControl.Type.MASTER_GAIN) as FloatControl
            gains[file]!!.value = volume * songVolume * (MaxGain - MinGain) + MinGain
            clip.framePosition = start().toInt()
            clip.start()
            true
        } else {
            println("$file not found")
            false
        }
    }
    fun setVolume(volume: Float) {
        this.volume = volume
        for (i in gains.values) {
            i.value = volume * (MaxGain - MinGain) + MinGain
        }
    }
    fun stopSound(sound: File) {
        loadedSounds[sound]?.close()
        loadedSounds.remove(sound)
    }
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