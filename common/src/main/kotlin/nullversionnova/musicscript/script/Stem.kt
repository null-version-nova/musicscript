package nullversionnova.musicscript.script

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class Stem(val name: String,val path: String) {
    class StemDeserializer : JsonDeserializer<Stem> {
        override fun deserialize(json: JsonElement, type: Type, context: JsonDeserializationContext): Stem {
            val obj = json.asJsonObject
            return Stem(obj["name"].asString,obj["path"].asString)
        }
    }
}