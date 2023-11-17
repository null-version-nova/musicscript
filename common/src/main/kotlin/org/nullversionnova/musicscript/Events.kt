package org.nullversionnova.musicscript

import dev.architectury.event.events.client.ClientLifecycleEvent
import dev.architectury.event.events.client.ClientPlayerEvent

object Events {
    fun init() {
        ClientLifecycleEvent.CLIENT_STARTED.register {
            println("Client started!")
            SoundManager.init(it)
        }
        ClientPlayerEvent.CLIENT_PLAYER_JOIN.register {
            PythonScriptManager.run("join-world.py")
            println("Player joined!")
        }
    }
}