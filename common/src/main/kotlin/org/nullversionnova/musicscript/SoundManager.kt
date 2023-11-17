package org.nullversionnova.musicscript

import dev.architectury.registry.registries.Registries
import net.minecraft.client.Minecraft
import net.minecraft.client.sounds.MusicManager
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.Music
import net.minecraft.sounds.SoundEvent
import java.io.File

object SoundManager {
    private lateinit var minecraft : Minecraft
    fun init(minecraft: Minecraft) {
        this.minecraft = minecraft
    }
    fun playSound(sound: ResourceLocation) : Boolean {
        minecraft.musicManager.startPlaying(Music(Registry.SOUND_EVENT.get(sound),0,0,true))
        return true
    }
    fun playSound(sound: String) : Boolean {
        return if (sound.contains(':')) {
            playSound(ResourceLocation(sound))
        } else {
            playSound(File("${MusicScript.MUSIC_PATH}/$sound"))
        }
    }
    fun playSound(sound: File) : Boolean {
        return if (sound.extension == "wav" || sound.extension == "aiff" || sound.extension == "au") {
            RawSoundEngine.start(sound)
        } else {
            false
        }
    }
    fun stopSounds() {
        RawSoundEngine.stopAll()
        minecraft.musicManager.stopPlaying()
    }
}