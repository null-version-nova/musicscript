package org.nullversionnova.musicscript

import dev.architectury.platform.Platform
import org.nullversionnova.musicscript.script.Events
import org.python.util.PythonInterpreter
import java.io.File
import java.util.*

object MusicScript {
    const val MOD_ID = "musicscript"
    val DATA_PATH = "${Platform.getGameFolder()}/${MOD_ID}"
    val properties = Properties()

    fun init() {
         PythonInterpreter.initialize(null,null, null)
        val propertyFile = File("${Platform.getConfigFolder()}/$MOD_ID/musicscript.properties")
        if (!File(DATA_PATH).isDirectory) { File(DATA_PATH).mkdir() }
        if (propertyFile.exists()) {
            properties.load(propertyFile.inputStream())
        } else {
            setDefaultConfig()
        }
        Events.init()
    }

    fun setDefaultConfig() {
        properties["song_path"] = "$DATA_PATH/songs"
        properties["script_path"] = DATA_PATH
        properties["max_volume"] = -14
        properties["min_volume"] = -36
        properties["delay_entering_world"] = 200
        properties["delay_after_song"] = 2000
    }
}