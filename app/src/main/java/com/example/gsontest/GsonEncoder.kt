package com.example.gsontest

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonBuilder


class GsonEncoder:Encoder {

    fun getJson():Json{
        return Json
    }

    /*override fun encode( class:Class<*>,codable: KSerializer<*>):String {

       return getGson().encodeToString((codable as SerializationStrategy<class>),codable)
    }*/

    override fun encode(codable: Encodable): String {
     return Json.encodeToString(codable)
    }

    override fun encode(codable: String,key:String):String {
        val container = getContainerWithKeys()
        container.encode(codable,key)
        return getJson().encodeToString(Container.serializer(),container)
    }

    override fun getContainerWithKeys():Container {
        val map = mutableMapOf<String, String>()
        val container = Container(map)
        return container
    }

}