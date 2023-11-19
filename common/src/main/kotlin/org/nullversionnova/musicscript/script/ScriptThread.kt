package org.nullversionnova.musicscript.script

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.registry.Registry
import org.nullversionnova.musicscript.MusicScript
import org.nullversionnova.musicscript.sound.SoundManager
import org.python.core.PyDictionary
import org.python.util.PythonInterpreter
import java.io.File

class ScriptThread(val scriptName: String, val player: PlayerEntity) : Thread() {
    override fun run() {
        val interpreter = PythonInterpreter()
        val script = try {
            File("${MusicScript.properties["script_path"]}/$scriptName").readText()
        } catch (e: Exception) {
            return
        }
        interpreter.set("data", setData(player))
        interpreter.exec(script)
        val commands = try {
            interpreter.get("output").toString().split(';')
        } catch (e: Exception) {
            return
        }
        for (i in commands) {
            execute(i.trim(), player)
        }
    }
    private fun execute(command : String, player: PlayerEntity) : Boolean {
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
    private fun setData(player: PlayerEntity) : PyDictionary {
        val data = PyDictionary()
        // Player properties //
        data["x"] = player.x // Float
        data["y"] = player.y // Float
        data["z"] = player.z // Float
        data["is_in_water"] = player.isSubmergedInWater

        // World properties //
        data["biome"] = player.world.registryManager.get(Registry.BIOME_KEY).getId(player.world.getBiome(player.blockPos).value()).toString()
        data["biome_warmth"] = player.world.getBiome(player.blockPos).value().temperature
        data["dimension"] = player.world.registryManager.get(Registry.DIMENSION_TYPE_KEY).getId(player.world.dimension).toString()
        data["time"] = player.world.timeOfDay
        return data
    }
}