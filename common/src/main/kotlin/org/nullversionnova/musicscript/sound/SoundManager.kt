package org.nullversionnova.musicscript.sound

import net.minecraft.util.Identifier
import org.nullversionnova.musicscript.MusicScript
import java.io.File

object SoundManager {
    private var isPaused = false
    @JvmStatic
    fun isPaused() : Boolean {
        return isPaused
    }
    @JvmStatic
    fun playSound(sound: String) : Boolean {
        if (sound.contains('*')) {
            sound.replace('*',' ')
        }
        return if (sound.contains(':')) {
            MinecraftMusicManager.playSound(Identifier(sound))
        } else {
            ExternalSoundManager.playSound(File("${MusicScript.properties["song_path"]}/$sound"))
        }
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