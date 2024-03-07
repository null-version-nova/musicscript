package nullversionnova.musicscript.channels

import nullversionnova.musicscript.script.PythonScriptManager
import nullversionnova.musicscript.sound.SoundManager

object MainChannel {
    @JvmStatic
    val script = PythonScriptManager()
    @JvmStatic
    val sounds = SoundManager()
}