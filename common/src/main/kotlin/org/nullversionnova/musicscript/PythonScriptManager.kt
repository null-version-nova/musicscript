package org.nullversionnova.musicscript

import org.python.core.PyDictionary
import org.python.util.PythonInterpreter
import java.io.File

object PythonScriptManager {
    val SCRIPT_DIRECTORY = MusicScript.DATA_PATH
    val data = PyDictionary()
    fun run(scriptName: String) : List<String> {
        println("Running $scriptName!")
        val interpreter = PythonInterpreter()
        val script = File("$SCRIPT_DIRECTORY/$scriptName").readText()
        interpreter.set("data", data)
        println(script)
        interpreter.exec(script)
        val commands = interpreter.get("output").toString().split(';')
        for (i in commands) {
            println(i)
            i.trim()
            println(i)
            execute(i)
        }
        return commands
    }
    fun execute(command : String) {
        val components = command.split(' ')
        when (components[0]) {
            "play" -> SoundManager.playSound(components[1])
            "stop" -> SoundManager.stopSounds()
        }
    }
}