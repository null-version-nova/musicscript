package org.nullversionnova.musicscript.forge

import dev.architectury.platform.forge.EventBuses
import net.minecraftforge.fml.common.Mod
import org.nullversionnova.musicscript.MusicScript
import org.nullversionnova.musicscript.script.PythonScriptManager
import thedarkcolour.kotlinforforge.forge.MOD_BUS

@Mod(MusicScript.MOD_ID)
object MusicScriptForge {
    init {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(MusicScript.MOD_ID, MOD_BUS)
        MusicScript.init()
        PythonScriptManager.initWorld()
    }
}