package org.nullversionnova.musicscript.fabric

import net.fabricmc.loader.api.FabricLoader
import java.nio.file.Path

object MusicScriptExpectPlatformImpl {
    /**
     * This is our actual method to [ExampleExpectPlatform.getConfigDirectory].
     */
    @JvmStatic // Jvm Static is required so that java can access it
    fun getConfigDirectory(): Path {
        return FabricLoader.getInstance().configDir
    }
}