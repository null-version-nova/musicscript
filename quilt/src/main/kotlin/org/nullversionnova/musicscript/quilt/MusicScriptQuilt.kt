package org.nullversionnova.musicscript.quilt

import org.nullversionnova.musicscript.fabriclike.MusicScriptFabricLike
import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer

object MusicScriptQuilt: ModInitializer {
    override fun onInitialize(mod: ModContainer?) {
        MusicScriptFabricLike.init()
    }
}