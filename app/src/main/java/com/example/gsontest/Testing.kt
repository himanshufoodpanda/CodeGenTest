package com.example.gsontest

import com.google.gson.*
import com.google.gson.annotations.SerializedName
import java.lang.reflect.Type

class Testing(
    @SerializedName("vendorId")
    val vendorId: String,
    @SerializedName("name")
    val name: String?
) : Selection {
    override var request: String
        get() = "REQUEST"
        set(value) {}
    override var post: String
        get() = "POST"
        set(value) {}
}

open class Campaign:JsonSerializer<Campaign>{

    @SerializedName("helo")
    val vendorId:String = "WOW"

    @SerializedName("TIME")
    val time:String = "Time"

    override fun serialize(
        src: Campaign?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        val jsonElement = Gson().toJsonTree(this)
        return jsonElement
    }
}