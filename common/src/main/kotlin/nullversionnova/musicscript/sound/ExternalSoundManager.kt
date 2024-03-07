package nullversionnova.musicscript.sound

import io.nayuki.flac.SimpleDecodeFlacToWav
import javazoom.jl.converter.Converter
import nullversionnova.musicscript.MusicScript
import java.io.BufferedInputStream
import java.io.File

class ExternalSoundManager {
    val rawSoundEngine = RawSoundEngine()
    fun playSound(sound: File, songVolume: Float = 1.0f, loop : Boolean = false, start: () -> Long = {0}) : Boolean {
        return when (sound.extension) {
            "wav", "aiff", "au" -> {
                rawSoundEngine.start(sound,sound.inputStream(),songVolume,loop,start)
            }
            "flac" -> {
                File("${MusicScript.DATA_PATH}/temp/flac").mkdirs()
                val dummyfile = File("${MusicScript.DATA_PATH}/temp/flac/${sound.nameWithoutExtension}.wav")
                if (dummyfile.createNewFile()) {
                    SimpleDecodeFlacToWav.main(arrayOf(sound.absolutePath, dummyfile.absolutePath))
                }
                dummyfile.deleteOnExit()
                rawSoundEngine.start(sound, BufferedInputStream(dummyfile.inputStream()),songVolume,loop,start)
            }
            "mp3" -> { // Not certain if this will get implemented.
                val converter = Converter()
                File("${MusicScript.DATA_PATH}/temp/mp3").mkdirs()
                val dummyfile = File("${MusicScript.DATA_PATH}/temp/mp3/${sound.nameWithoutExtension}.wav")
                if (dummyfile.createNewFile()) {
                    converter.convert(sound.absolutePath,dummyfile.absolutePath)
                }
                dummyfile.deleteOnExit()
                rawSoundEngine.start(sound, BufferedInputStream(dummyfile.inputStream()),songVolume,loop,start)
            }
            "ogg" -> {
                false
            }
            else -> {
                false
            }
        }
    }
    fun stopSounds() {
        rawSoundEngine.stopSounds()
    }
    fun pause() {
        rawSoundEngine.pause()
    }
    fun resume() {
        rawSoundEngine.resume()
    }
    fun isAnythingPlaying() : Boolean {
        return rawSoundEngine.isAnythingPlaying()
    }
}