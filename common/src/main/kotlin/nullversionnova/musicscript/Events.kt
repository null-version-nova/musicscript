package nullversionnova.musicscript

import dev.architectury.event.events.client.ClientPlayerEvent
import dev.architectury.event.events.client.ClientTickEvent
import dev.architectury.event.events.common.PlayerEvent
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.client.sound.MusicType
import net.minecraft.data.Main
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.registry.RegistryKey
import net.minecraft.world.World
import nullversionnova.musicscript.channels.MainChannel
import nullversionnova.musicscript.script.PythonScriptManager
import nullversionnova.musicscript.sound.SoundManager

object Events {
    var ticks = 50
    private var isPlayerSubmerged = false
    fun init() {
        PlayerEvent.CHANGE_DIMENSION.register { serverPlayerEntity: ServerPlayerEntity, _: RegistryKey<World>, _: RegistryKey<World> ->
            if (MinecraftClient.getInstance().player!!.uuid == serverPlayerEntity.uuid) { // Is this necessary? Who knows.
                MainChannel.script.run("change_dimension", serverPlayerEntity,MainChannel.sounds)
            }
        }
        PlayerEvent.PLAYER_RESPAWN.register { serverPlayerEntity: ServerPlayerEntity, b: Boolean ->
            if (MinecraftClient.getInstance().player!!.uuid == serverPlayerEntity.uuid) {
                if (b) {
                    MainChannel.sounds.stopSounds()
                    MainChannel.script.run("change_dimension", serverPlayerEntity,MainChannel.sounds)
                } else {
                    MainChannel.script.run("player_respawn", serverPlayerEntity,MainChannel.sounds)
                }
            }
        }
        ClientTickEvent.CLIENT_POST.register {
            musicTick(it.player)
            if (it.player != null) {
                if (isPlayerSubmerged != it.player!!.isSubmergedInWater) {
                    isPlayerSubmerged = it.player!!.isSubmergedInWater
                    MainChannel.script.run("water_submersion", it.player!!,MainChannel.sounds)
                }
            }
        }
        ClientPlayerEvent.CLIENT_PLAYER_JOIN.register {
            isPlayerSubmerged = it.isSubmergedInWater
            MainChannel.script.initWorld()
        }
        ClientPlayerEvent.CLIENT_PLAYER_QUIT.register {
            MainChannel.script.close()
            MainChannel.sounds.stopSounds()
            ticks = 50
        }
    }
    fun playSong(player: ClientPlayerEntity) {
        if (ticks == 0) {
            ticks = 50
            MainChannel.script.run("play_song", player,MainChannel.sounds)
        } else if (!MainChannel.sounds.isPaused()) {
            decrement()
        }
    }
    fun playCredits() {
        if (!MainChannel.sounds.isAnythingPlaying()) {
            MainChannel.sounds.playSound("minecraft:music.credits")
        }
    }
    fun playMenu() {
        if (ticks == 0 && !MainChannel.sounds.isAnythingPlaying()) {
            ticks = 50
            MainChannel.sounds.playSound("minecraft:music.menu")
        } else {
            decrement()
        }
    }
    fun decrement() {
        if (!MainChannel.sounds.isAnythingPlaying()) {
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