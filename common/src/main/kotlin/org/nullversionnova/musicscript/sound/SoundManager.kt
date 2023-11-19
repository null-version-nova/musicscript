package org.nullversionnova.musicscript.sound

import net.minecraft.client.MinecraftClient
import net.minecraft.util.Identifier
import org.apache.commons.compress.utils.BitInputStream
import org.nullversionnova.musicscript.MusicScript
import org.nullversionnova.musicscript.SimpleDecodeFlacToWav
import org.nullversionnova.musicscript.SimpleDecodeFlacToWav.decodeFile
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.PipedInputStream
import java.io.PipedOutputStream

object SoundManager {
    private var isPaused = false
    @JvmStatic
    fun isPaused() : Boolean {
        return isPaused
    }
    @JvmStatic
    fun playSound(sound: String) : Boolean {
        return if (sound.contains(':')) {
            MinecraftMusicManager.playSound(Identifier(sound))
        } else {
            playSound(File("${MusicScript.properties["song_path"]}/$sound"))
        }
    }
    @JvmStatic
    fun playSound(sound: File) : Boolean {
        return when (sound.extension) {
            "wav", "aiff", "au" -> {
                RawSoundEngine.start(sound)
            }

            "flac" -> {
                val dummyfile = File("${MusicScript.DATA_PATH}/deadweight.wav")
                dummyfile.createNewFile()
                SimpleDecodeFlacToWav.main(arrayOf(sound.absolutePath, dummyfile.absolutePath))
                dummyfile.deleteOnExit()
                RawSoundEngine.start(dummyfile)
            }

            else -> {
                false
            }
        }
    }
    @JvmStatic
    fun stopSounds() {
        RawSoundEngine.stopAll()
        MinecraftMusicManager.stopSounds()
    }
    @JvmStatic
    fun pause() {
        isPaused = true
        RawSoundEngine.pause()
        MinecraftClient.getInstance().soundManager.pauseAll()
    }
    @JvmStatic
    fun resume() {
        isPaused = false
        RawSoundEngine.resume()
        MinecraftClient.getInstance().soundManager.resumeAll()
    }
    @JvmStatic
    fun isAnythingPlaying() : Boolean {
        return RawSoundEngine.isAnythingPlaying() || MinecraftMusicManager.isAnythingPlaying()
    }
}