package org.nullversionnova.musicscript.script

import net.minecraft.entity.player.PlayerEntity
import org.nullversionnova.musicscript.MusicScript
import org.python.util.PythonInterpreter
import java.io.File


object PythonScriptManager {
    val interpreter = PythonInterpreter()
    var enabled = false
    fun init() {
        enabled = try {
            interpreter.exec(File("${MusicScript.DATA_PATH}/musicscript.py").readText())
            true
        } catch (e: Exception) {
            println(e)
            false
        }
    }
    fun close() {
        interpreter.cleanup()
    }
    @JvmStatic
    fun run(scriptName: String, player: PlayerEntity) {
        if (enabled) {
            ScriptThread(interpreter, scriptName, player).start()
        }
    }
}