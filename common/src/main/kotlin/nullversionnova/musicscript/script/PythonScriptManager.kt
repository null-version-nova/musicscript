package nullversionnova.musicscript.script

import net.minecraft.entity.player.PlayerEntity
import nullversionnova.musicscript.MusicScript
import nullversionnova.musicscript.sound.SoundManager
import org.python.core.PyClass
import org.python.util.PythonInterpreter
import java.io.File
import java.util.Properties

class PythonScriptManager {
    private val interpreter : PythonInterpreter
    private var dataClass : PyClass? = null
    var enabled = false
    init {
        val postProperties = Properties()
        postProperties["python.home"] = "./lib"
        postProperties["python.import.site"] = "false"
        PythonInterpreter.initialize(System.getProperties(),postProperties, arrayOf<String>())
        interpreter = PythonInterpreter()
    }
    fun initWorld() {
        enabled = try {
            interpreter.exec(File("${MusicScript.DATA_PATH}/musicscript.py").readText())
            dataClass = interpreter["Data"] as PyClass
            true
        } catch (e: Exception) {
            println(e)
            false
        }
    }
    fun close() {
        interpreter.cleanup()
    }
    fun run(scriptName: String, player: PlayerEntity, manager: SoundManager) {
        if (enabled) {
            ScriptThread(interpreter, scriptName, player, dataClass!!,manager).start()
        }
    }
}