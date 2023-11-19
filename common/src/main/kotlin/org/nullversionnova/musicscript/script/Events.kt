package org.nullversionnova.musicscript.script

import dev.architectury.event.events.client.ClientPlayerEvent
import dev.architectury.event.events.client.ClientTickEvent
import dev.architectury.event.events.common.PlayerEvent
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.client.sound.MusicType
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.registry.RegistryKey
import net.minecraft.world.World
import org.nullversionnova.musicscript.sound.SoundManager

object Events {
    var ticks = 200
    var isPlayerSubmerged = false
    fun init() {
        PlayerEvent.CHANGE_DIMENSION.register { serverPlayerEntity: ServerPlayerEntity, _: RegistryKey<World>, _: RegistryKey<World> ->
            SoundManager.stopSounds()
            PythonScriptManager.run("change-dimension.py", serverPlayerEntity)
        }
        PlayerEvent.PLAYER_RESPAWN.register { serverPlayerEntity: ServerPlayerEntity, b: Boolean ->
            if (b) {
                SoundManager.stopSounds()
                PythonScriptManager.run("change-dimension.py", serverPlayerEntity)
            } else {
                PythonScriptManager.run("player-respawn.py", serverPlayerEntity)
            }
        }
        ClientTickEvent.CLIENT_POST.register {
            it.player?.let { it1 -> musicTick(it1) }
            if (it.player != null) {
                if (isPlayerSubmerged != it.player!!.isSubmergedInWater) {
                    isPlayerSubmerged = it.player!!.isSubmergedInWater
                    PythonScriptManager.run("water-submersion.py", it.player!!)
                }
            }

        }
        ClientPlayerEvent.CLIENT_PLAYER_JOIN.register {
            isPlayerSubmerged = it.isSubmergedInWater
        }
        ClientPlayerEvent.CLIENT_PLAYER_QUIT.register {
            SoundManager.stopSounds()
            ticks = 200
        }
    }
    fun playSong(player: ClientPlayerEntity) {
        if (ticks == 0) {
            ticks = 2000
            PythonScriptManager.run("play-song.py", player)
        } else if (!SoundManager.isAnythingPlaying()) {
            ticks--
        }
    }
    fun playCredits() {
        if (!SoundManager.isAnythingPlaying()) {
            SoundManager.playSound("minecraft:music.credits")
        }
    }
    fun playMenu() {
        if (ticks == 0 && !SoundManager.isAnythingPlaying()) {
            ticks = 200
            SoundManager.playSound("minecraft:music.menu")
        } else {
            decrement()
        }
    }
    fun decrement() {
        if (!SoundManager.isAnythingPlaying()) {
            ticks--
        }
    }
    fun musicTick(player: ClientPlayerEntity) {
        val music = MinecraftClient.getInstance().musicType
        if (music == MusicType.CREDITS) {
            playCredits()
        } else if (music == MusicType.MENU) {
            playMenu()
        } else {
            playSong(player)
        }
    }
}