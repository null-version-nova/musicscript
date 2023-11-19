package org.nullversionnova.musicscript.script

import net.minecraft.entity.player.PlayerEntity


object PythonScriptManager {
    @JvmStatic
    fun run(scriptName: String, player: PlayerEntity) {
        ScriptThread(scriptName, player).start()
    }
}