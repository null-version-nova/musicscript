package org.nullversionnova.musicscript.forge

import net.minecraftforge.fml.loading.FMLPaths
import java.nio.file.Path

object MusicScriptExpectPlatformImpl {
    /**
     * This is our actual method to [MusicScriptExpectPlatform.getConfigDirectory].
     */
    @JvmStatic // Jvm Static is required so that java can access it
    fun getConfigDirectory(): Path {
        return FMLPaths.CONFIGDIR.get()
    }
}