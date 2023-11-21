package org.nullversionnova.musicscript.sound

import io.nayuki.flac.SimpleDecodeFlacToWav
import javazoom.jl.converter.Converter
import org.nullversionnova.musicscript.MusicScript
import java.io.BufferedInputStream
import java.io.File

object ExternalSoundManager {
    @JvmStatic
    fun playSound(sound: File) : Boolean {
        return when (sound.extension) {
            "wav", "aiff", "au" -> {
                RawSoundEngine.start(sound)
            }
            "flac" -> {
                File("${MusicScript.DATA_PATH}/temp/flac").mkdirs()
                val dummyfile = File("${MusicScript.DATA_PATH}/temp/flac/${sound.nameWithoutExtension}.wav")
                if (dummyfile.createNewFile()) {
                    SimpleDecodeFlacToWav.main(arrayOf(sound.absolutePath, dummyfile.absolutePath))
                }
                dummyfile.deleteOnExit()
                RawSoundEngine.start(sound, BufferedInputStream(dummyfile.inputStream()))
            }
            "mp3" -> { // Not certain if this will get implemented.
                val converter = Converter()
                val dummyfile = File("${MusicScript.DATA_PATH}/temp/mp3/${sound.nameWithoutExtension}.wav")
                if (dummyfile.createNewFile()) {
                    converter.convert(sound.absolutePath,dummyfile.absolutePath)
                }
                dummyfile.deleteOnExit()
                RawSoundEngine.start(sound, BufferedInputStream(dummyfile.inputStream()))
            }
            "ogg" -> {
                false
            }
            else -> {
                false
            }
        }
    }
    @JvmStatic
    fun stopSounds() {
        RawSoundEngine.stopSounds()
    }
    @JvmStatic
    fun pause() {
        RawSoundEngine.pause()
    }
    @JvmStatic
    fun resume() {
        RawSoundEngine.resume()
    }
    @JvmStatic
    fun isAnythingPlaying() : Boolean {
        return RawSoundEngine.isAnythingPlaying()
    }
}