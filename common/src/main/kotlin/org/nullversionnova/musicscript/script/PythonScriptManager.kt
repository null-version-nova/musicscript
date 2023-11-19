package org.nullversionnova.musicscript.script

import net.minecraft.entity.player.PlayerEntity
import org.nullversionnova.musicscript.MusicScript
import org.python.util.PythonInterpreter
import java.io.File


object PythonScriptManager {
    val interpreter = PythonInterpreter()
    fun init() {
        interpreter.exec(File("${MusicScript.DATA_PATH}/musicscript.py").readText())
    }
    @JvmStatic
    fun run(scriptName: String, player: PlayerEntity) {
        ScriptThread(interpreter, scriptName, player).start()
    }
}