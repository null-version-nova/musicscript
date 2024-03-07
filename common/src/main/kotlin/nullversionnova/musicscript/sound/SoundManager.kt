package nullversionnova.musicscript.sound

import net.minecraft.util.Identifier
import nullversionnova.musicscript.MusicScript
import nullversionnova.musicscript.script.Songs
import java.io.File

class SoundManager {
    val externalSoundManager = ExternalSoundManager()
    val complexSongManager = ComplexSongManager()
    private var isPaused = false
    fun isPaused() : Boolean {
        return isPaused
    }
    fun playSound(sound: String) : Boolean {
        return if (sound.contains(':')) {
            MinecraftMusicManager.playSound(Identifier(sound))
        } else if (Songs.hasSong(sound)) {
            complexSongManager.playSong(sound)
            if (Songs.getSong(sound)!!["main"] != null) {
                complexSongManager.enableStem("main")
                true
            } else {
                true
            }
        } else {
            externalSoundManager.playSound(File("${MusicScript.properties["song_path"]}/$sound"))
        }
    }
    fun stopSounds() {
        externalSoundManager.stopSounds()
        complexSongManager.stop()
        MinecraftMusicManager.stopSounds()
    }
    fun pause() {
        isPaused = true
        complexSongManager.pause()
        externalSoundManager.pause()
        MinecraftMusicManager.pause()
    }
    fun resume() {
        isPaused = false
        complexSongManager.resume()
        externalSoundManager.resume()
        MinecraftMusicManager.resume()
    }
    fun isAnythingPlaying() : Boolean {
        return externalSoundManager.isAnythingPlaying() || MinecraftMusicManager.isAnythingPlaying() || complexSongManager.isPlaying
    }
}