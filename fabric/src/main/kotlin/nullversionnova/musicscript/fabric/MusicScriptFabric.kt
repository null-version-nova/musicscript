package nullversionnova.musicscript.fabric

import net.fabricmc.api.ModInitializer
import nullversionnova.musicscript.MusicScript


object MusicScriptFabric: ModInitializer {
    override fun onInitialize() {
        MusicScript.init()
    }
}
