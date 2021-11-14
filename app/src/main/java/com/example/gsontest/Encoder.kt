package com.example.gsontest

import kotlinx.serialization.Serializable


interface Codable : Encodable {
    fun encode(): String
}

@Serializable(with = EncodableSerialiser::class)
interface Encodable {

}

interface Encoder {
    fun encode(codable: Encodable): String
    fun encode(codable: String, key: String): String
    fun getContainerWithKeys(): Container
}

@Serializable
class Container(val map: MutableMap<String, String>) {
    fun encode(value: String, key: String) {
        map.putIfAbsent(key, value)
    }

    fun getContainerMap(): Map<String, String> {
        return map
    }
}




