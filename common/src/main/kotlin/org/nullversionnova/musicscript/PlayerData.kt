package org.nullversionnova.musicscript

import net.minecraft.client.MinecraftClient

object PlayerData {
    val instance = MinecraftClient.getInstance()
    val player = ::instance.get().player
}