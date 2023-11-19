package org.nullversionnova.musicscript.sound

import net.minecraft.client.MinecraftClient
import net.minecraft.client.sound.PositionedSoundInstance
import net.minecraft.sound.MusicSound
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object MinecraftMusicManager {
    private val musicRegistry = mutableMapOf<MusicSound, PositionedSoundInstance>()
    @JvmStatic
    fun playSound(sound: Identifier) : Boolean {
        for (i in musicRegistry.keys) {
            if (sound == i.sound.id) {
                if (musicRegistry[i] != null) {
                    MinecraftClient.getInstance().soundManager.play(musicRegistry[i])
                }
                return true
            }
        }
        val music = MusicSound(Registry.SOUND_EVENT.get(sound),0,0,true)
        musicRegistry[music] = PositionedSoundInstance.music(music.sound)
        if (musicRegistry[music] != null) {
            MinecraftClient.getInstance().soundManager.play(musicRegistry[music])
        }
        return true
    }
    fun stopSound(sound: PositionedSoundInstance) {
        MinecraftClient.getInstance().soundManager.stop(sound)
    }
    fun stopSounds() {
        for (i in musicRegistry.values) {
            stopSound(i)
        }
    }
    fun pause() {
        MinecraftClient.getInstance().soundManager.pauseAll()
    }
    fun resume() {
        MinecraftClient.getInstance().soundManager.resumeAll()
    }
    fun isAnythingPlaying() : Boolean {
        for (i in musicRegistry.values) {
            if (MinecraftClient.getInstance().soundManager.isPlaying(i)) { return true }
        }
        return false
    }
}