package nullversionnova.musicscript.script

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.registry.Registry
import nullversionnova.musicscript.Events
import nullversionnova.musicscript.sound.SoundManager
import org.python.core.PyBoolean
import org.python.core.PyClass
import org.python.core.PyDictionary
import org.python.core.PyFloat
import org.python.core.PyInstance
import org.python.core.PyInteger
import org.python.core.PyLong
import org.python.core.PyObject
import org.python.core.PyString
import org.python.util.PythonInterpreter

class ScriptThread(private val interpreter: PythonInterpreter, private val scriptName: String, private val player: PlayerEntity, private val data: PyClass) : Thread() {
    override fun run() {
        interpreter.set("data",setData(player,data))
        val commands = try {
            interpreter.exec("output = $scriptName(data)")
            interpreter.get("output").toString().split(';')
        } catch (e: Exception) {
            return
        }
        for (i in commands) {
            execute(i.trim())
        }
    }
    private fun execute(command : String) : Boolean {
        println(command)
        val components = command.split(' ')
        when (components[0]) {
            "play" -> {
                return if (!SoundManager.isAnythingPlaying()) {
                    SoundManager.playSound(components[(1 until components.size).random()])
                    true
                } else {
                    false
                }
            }
            "playstem" -> {
                val song = components[1]
                val stem = components[2]
            }
            "stop" -> SoundManager.stopSounds()
            "pause" -> SoundManager.pause()
            "resume" -> SoundManager.resume()
            "delay" -> {
                if (components.size == 2) {
                    Events.ticks = components[1].toInt()
                } else {
                    Events.ticks = (components[1].toInt()..components[2].toInt()).random()
                }
            }
        }
        return false
    }
    private fun setData(player: PlayerEntity, clazz: PyClass) : PyInstance {
        val data = PyInstance(clazz)
        // Player properties //
        data.__setattr__("x",PyFloat(player.x))
        data.__setattr__("y",PyFloat(player.y))
        data.__setattr__("z",PyFloat(player.z))
        data.__setattr__("is_in_water",PyBoolean(player.isSubmergedInWater))

        // World properties //
        data.__setattr__("difficulty",PyInteger(player.world.difficulty.ordinal))
        data.__setattr__("biome",PyString(player.world.registryManager.get(Registry.BIOME_KEY).getId(player.world.getBiome(player.blockPos).value()).toString()))
        data.__setattr__("biome_warmth",PyFloat(player.world.getBiome(player.blockPos).value().temperature))
        data.__setattr__("dimension",PyString(player.world.registryManager.get(Registry.DIMENSION_TYPE_KEY).getId(player.world.dimension).toString()))
        data.__setattr__("time",PyLong(player.world.timeOfDay))
        data.__setattr__("weather",PyString(if (player.world.isThundering) {
            "thunder"
        } else if (player.world.isRaining) {
            "rain"
        } else {
            "clear"
        }))
        return data
    }
}