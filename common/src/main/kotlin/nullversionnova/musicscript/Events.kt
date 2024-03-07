package nullversionnova.musicscript

import dev.architectury.event.events.client.ClientPlayerEvent
import dev.architectury.event.events.client.ClientTickEvent
import dev.architectury.event.events.common.PlayerEvent
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.client.sound.MusicType
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.registry.RegistryKey
import net.minecraft.world.World
import nullversionnova.musicscript.script.PythonScriptManager
import nullversionnova.musicscript.sound.SoundManager

object Events {
    var ticks = 50
    private var isPlayerSubmerged = false
    fun init() {
        PlayerEvent.CHANGE_DIMENSION.register { serverPlayerEntity: ServerPlayerEntity, _: RegistryKey<World>, _: RegistryKey<World> ->
            if (MinecraftClient.getInstance().player!!.uuid == serverPlayerEntity.uuid) { // Is this necessary? Who knows.
                SoundManager.stopSounds()
                PythonScriptManager.run("change_dimension", serverPlayerEntity)
            }
        }
        PlayerEvent.PLAYER_RESPAWN.register { serverPlayerEntity: ServerPlayerEntity, b: Boolean ->
            if (MinecraftClient.getInstance().player!!.uuid == serverPlayerEntity.uuid) {
                if (b) {
                    SoundManager.stopSounds()
                    PythonScriptManager.run("change_dimension", serverPlayerEntity)
                } else {
                    PythonScriptManager.run("player_respawn", serverPlayerEntity)
                }
            }
        }
        ClientTickEvent.CLIENT_POST.register {
            musicTick(it.player)
            if (it.player != null) {
                if (isPlayerSubmerged != it.player!!.isSubmergedInWater) {
                    isPlayerSubmerged = it.player!!.isSubmergedInWater
                    PythonScriptManager.run("water_submersion", it.player!!)
                }
            }
        }
        ClientPlayerEvent.CLIENT_PLAYER_JOIN.register {
            isPlayerSubmerged = it.isSubmergedInWater
            PythonScriptManager.initWorld()
        }
        ClientPlayerEvent.CLIENT_PLAYER_QUIT.register {
            PythonScriptManager.close()
            SoundManager.stopSounds()
            ticks = 50
        }
    }
    fun playSong(player: ClientPlayerEntity) {
        if (ticks == 0) {
            ticks = 50
            PythonScriptManager.run("play_song", player)
        } else if (!SoundManager.isPaused()) {
            decrement()
        }
    }
    fun playCredits() {
        if (!SoundManager.isAnythingPlaying()) {
            SoundManager.playSound("minecraft:music.credits")
        }
    }
    fun playMenu() {
        if (ticks == 0 && !SoundManager.isAnythingPlaying()) {
            ticks = 50
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
    fun musicTick(player: ClientPlayerEntity?) {
        val music = MinecraftClient.getInstance().musicType
        when (music) {
            MusicType.CREDITS -> {
                playCredits()
            }
            MusicType.MENU -> {
                playMenu()
            }
            else -> {
                if (player != null) {
                    playSong(player)
                }
            }
        }
    }
}