package com.example.gsontest

import com.google.gson.Gson
import com.google.gson.GsonBuilder


class GsonEncoder:Encoder {

    fun getGson():Gson{
        return GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create()
    }

    override fun encode(codable: Encodable):String {
       return getGson().toJson(codable)
    }

    override fun encode(codable: String,key:String):String {
        val container = getContainerWithKeys()
        container.encode(codable,key)
        return getGson().toJson(container.getContainerMap())
    }

    override fun getContainerWithKeys():Encoder.Container{
        val map = mutableMapOf<String,String>()
        val container = Container(map)
        return container
    }

    class Container(val map: MutableMap<String, String>) :Encoder.Container {
        override fun encode(codable: String, key: String) {
           map.putIfAbsent(key,codable)
        }

        override fun getContainerMap(): Map<String, String> {
            return map
        }
    }
}