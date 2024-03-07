package nullversionnova.musicscript.sound

import nullversionnova.musicscript.MusicScript
import nullversionnova.musicscript.script.Songs
import java.io.File

class ComplexSongManager {
    val externalSoundManager = ExternalSoundManager()
    var isPaused = false
    var isPlaying = false
    var currentSong = ""
    val currentStems = mutableSetOf<String>()
    fun playSong(song: String) {
        currentSong = song
        isPlaying = true
    }
    fun enableStem(stem: String) {
        currentStems.add(stem)
        val start = { if (externalSoundManager.rawSoundEngine.isAnythingPlaying()) {
            externalSoundManager.rawSoundEngine.loadedSounds.values.first().longFramePosition
        } else {
            0
        } }
        externalSoundManager.playSound(
            sound = File("${MusicScript.properties["song_path"]}/${Songs.getSong(currentSong)!![stem]!!.path}"),
            songVolume = Songs.getSong(currentSong)!![stem]!!.volume,
            loop = Songs.getSong(currentSong)!!.loop,
            start = start
        )
    }
    fun disableStem(stem: String) {
        if (currentStems.remove(stem)) {
            externalSoundManager.rawSoundEngine.stopSound(File("${MusicScript.properties["song_path"]}/${Songs.getSong(currentSong)!![stem]!!.path}"))
        }
    }
    fun pause() {
        isPaused = true
        externalSoundManager.pause()
    }
    fun resume() {
        isPaused = false
        externalSoundManager.resume()
    }
    fun stop() {
        externalSoundManager.stopSounds()
        isPlaying = false
        currentSong = ""
        currentStems.clear()
    }
}