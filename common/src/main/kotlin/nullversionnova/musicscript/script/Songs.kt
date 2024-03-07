package nullversionnova.musicscript.script

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import nullversionnova.musicscript.MusicScript
import java.io.File

object Songs {
    val songs = mutableListOf<Song>()
    fun init() {
        val file = File("${ MusicScript.DATA_PATH}/songs.json").readText()
        val gson = Gson()
        val builder = GsonBuilder()
        builder.registerTypeAdapter(Song::class.java,Song.SongDeserializer())
        val obj = gson.fromJson(file,JsonArray::class.java)
        for (i in obj) {
            songs.add(gson.fromJson(i,Song::class.java))
        }
    }
    fun hasSong(name: String) : Boolean {
        for (i in songs) {
            if (i.name == name) {
                return true
            }
        }
        return false
    }
    fun getSong(name: String) : Song? {
        for (i in songs) {
            if (i.name == name) {
                return i
            }
        }
        return null
    }
}