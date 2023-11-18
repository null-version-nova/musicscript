package org.nullversionnova.musicscript

import dev.architectury.event.events.client.ClientPlayerEvent
import dev.architectury.event.events.client.ClientTickEvent
import dev.architectury.event.events.common.PlayerEvent
import dev.architectury.event.events.common.TickEvent
import net.minecraft.client.MinecraftClient
import net.minecraft.client.sound.MusicType
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.registry.RegistryKey
import net.minecraft.world.World

object Events {
    var ticks = 200
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
            val music = MinecraftClient.getInstance().musicType
            if (music == MusicType.CREDITS) {
                if (!SoundManager.isAnythingPlaying()) { SoundManager.playSound("minecraft:music.credits") }
            } else if (music == MusicType.MENU) {
                if (ticks == 0 && !SoundManager.isAnythingPlaying()) {
                    ticks = 200
                    SoundManager.playSound("minecraft:music.menu")
                } else if (!SoundManager.isAnythingPlaying()) {
                    ticks--
                }
            } else if (ticks == 0) {
                ticks = 2000
                it.player?.let { it1 -> PythonScriptManager.run("play-song.py", it1) }
            } else if (!SoundManager.isAnythingPlaying()) {
                ticks--
            }
        }
        ClientPlayerEvent.CLIENT_PLAYER_QUIT.register {
            SoundManager.stopSounds()
            ticks = 200
        }
    }
}