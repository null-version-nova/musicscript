package nullversionnova.musicscript.sound

import net.minecraft.util.Identifier
import nullversionnova.musicscript.MusicScript
import nullversionnova.musicscript.script.Songs
import java.io.File

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
        } else if (Songs.hasSong(sound)) {
            ExternalSoundManager.playSound(File("${MusicScript.properties["song_path"]}/${Songs.getSong(sound)!!.stems["main"]!!.path}"))
        } else {
            ExternalSoundManager.playSound(File("${MusicScript.properties["song_path"]}/$sound"))
        }
    }
    fun playStem(song: String, stem: String) : Boolean {
        return false
    }
    @JvmStatic
    fun stopSounds() {
        ExternalSoundManager.stopSounds()
        MinecraftMusicManager.stopSounds()
    }
    @JvmStatic
    fun pause() {
        isPaused = true
        ExternalSoundManager.pause()
        MinecraftMusicManager.pause()
    }
    @JvmStatic
    fun resume() {
        isPaused = false
        ExternalSoundManager.resume()
        MinecraftMusicManager.resume()
    }
    @JvmStatic
    fun isAnythingPlaying() : Boolean {
        return ExternalSoundManager.isAnythingPlaying() || MinecraftMusicManager.isAnythingPlaying()
    }
}