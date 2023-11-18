package org.nullversionnova.musicscript

import net.minecraft.client.sound.PositionedSoundInstance
import net.minecraft.sound.MusicSound
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import java.io.File

object SoundManager {
    private val musicRegistry = mutableMapOf<MusicSound,PositionedSoundInstance>()
    private var isPaused = false
    @JvmStatic
    fun isPaused() : Boolean {
        return isPaused
    }
    @JvmStatic
    fun playSound(sound: Identifier) : Boolean {
        for (i in musicRegistry.keys) {
            if (sound == i.sound.id) {
                if (musicRegistry[i] != null) {
                    PlayerData.instance.soundManager.play(musicRegistry[i])
                }
                return true
            }
        }
        val music = MusicSound(Registry.SOUND_EVENT.get(sound),0,0,true)
        musicRegistry[music] = PositionedSoundInstance.music(music.sound)
        if (musicRegistry[music] != null) {
            PlayerData.instance.soundManager.play(musicRegistry[music])
        }
        return true
    }
    @JvmStatic
    fun playSound(sound: String) : Boolean {
        return if (sound.contains(':')) {
            playSound(Identifier(sound))
        } else {
            playSound(File("${MusicScript.properties["song_path"]}/$sound"))
        }
    }
    @JvmStatic
    fun playSound(sound: File) : Boolean {
        return if (sound.extension == "wav" || sound.extension == "aiff" || sound.extension == "au") {
            RawSoundEngine.start(sound)
        } else {
            false
        }
    }
    @JvmStatic
    fun stopSounds() {
        RawSoundEngine.stopAll()
        PlayerData.instance.musicTracker.stop()
    }
    @JvmStatic
    fun pause() {
        isPaused = true
        RawSoundEngine.pause()
        PlayerData.instance.soundManager.pauseAll()
    }
    @JvmStatic
    fun resume() {
        isPaused = false
        RawSoundEngine.resume()
        PlayerData.instance.soundManager.resumeAll()
    }
    @JvmStatic
    fun isAnythingPlaying() : Boolean {
        return if (RawSoundEngine.isAnythingPlaying()) {
            true
        } else {
            for (i in musicRegistry.keys) {
                if (PlayerData.instance.soundManager.isPlaying(musicRegistry[i])) { return true }
            }
            false
        }
    }
}