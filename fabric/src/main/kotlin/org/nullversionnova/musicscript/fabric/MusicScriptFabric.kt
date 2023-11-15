package org.nullversionnova.musicscript.fabric

import org.nullversionnova.musicscript.fabriclike.MusicScriptFabricLike
import net.fabricmc.api.ModInitializer


object MusicScriptFabric: ModInitializer {
    override fun onInitialize() {
        MusicScriptFabricLike.init()
    }
}
