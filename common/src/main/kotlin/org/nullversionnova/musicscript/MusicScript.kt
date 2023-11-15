package org.nullversionnova.musicscript

import org.nullversionnova.musicscript.MusicScriptExpectPlatform.getConfigDirectory

object MusicScript {
    const val MOD_ID = "musicscript"

    fun init() {
        println("CONFIG DIR: ${getConfigDirectory().toAbsolutePath().normalize()}")
    }
}