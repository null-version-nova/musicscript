package nullversionnova.musicscript.script

import com.google.gson.*
import java.lang.reflect.Type

class Song(val name: String) {
    val stems = mutableListOf<Stem>()
    var loop = false
    constructor() : this("default")
    class SongDeserializer : JsonDeserializer<Song> {
        override fun deserialize(json: JsonElement, type: Type, context: JsonDeserializationContext): Song {
            val obj = json.asJsonObject
//            val volume = if (obj["volume"].isJsonPrimitive) {
//                obj["volume"].asDouble
//            } else {
//                1.0
//            }
            val song = Song(obj["name"].asString)
            if (!obj["path"].isJsonNull) {
                song["main"] = Stem().apply {
                    name = "main"
                    path = obj["path"].asString
                }
            }
            if (obj["stems"].isJsonArray) {
                obj["stems"].asJsonArray.forEach {
                    val stem = Gson().fromJson(it,Stem::class.java)
                    song[stem.name] = stem
                }
            }
            song.loop = obj["loop"].asBoolean
            return song
        }
    }
    class Stem {
        var name = ""
        var path = ""
        val volume = 1.0f
//    class StemDeserializer : JsonDeserializer<Stem> {
//        override fun deserialize(json: JsonElement, type: Type, context: JsonDeserializationContext): Stem {
//            val obj = json.asJsonObject
//            val stem = Stem()
//            stem
//            return Stem(obj["name"].asString,obj["path"].asString)
//        }
//    }
    }

    operator fun get(stem: String) : Stem? {
        for (i in stems) {
            if (i.name == stem) {
                return i
            }
        }
        return null
    }
    operator fun set(index: String, stem: Stem) {
        if (get(index) == null) {
            stems.add(stem)
        } else {
            stems[stems.indexOf(get(index))] = stem
        }
    }
}