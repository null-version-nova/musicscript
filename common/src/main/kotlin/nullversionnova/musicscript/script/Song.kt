package nullversionnova.musicscript.script

import com.google.gson.JsonArray
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class Song(val name: String) {
    val stems = mutableMapOf<String,Stem>()
    constructor() : this("default")
    class SongDeserializer : JsonDeserializer<Song> {
        override fun deserialize(json: JsonElement, type: Type, context: JsonDeserializationContext): Song {
            val obj = json.asJsonObject
            val volume = if (obj["volume"].isJsonPrimitive) {
                obj["volume"].asDouble
            } else {
                1.0
            }
            val song = Song(obj["name"].asString)
            if (!obj["path"].isJsonNull) {
                song.stems["main"] = Stem("main",obj["path"].asString)
            }
            if (!obj["stems"].isJsonNull) {
                context.deserialize<JsonArray>(obj["stems"],JsonArray::class.java).forEach {
                    val stem = context.deserialize<Stem>(it,Stem::class.java)
                    song.stems[stem.name] = stem
                }
            }
            return song
        }
    }
}