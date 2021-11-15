package com.example.gsontest

import kotlinx.serialization.SerialName
import java.lang.reflect.Type

class Testing(
    @SerialName("vendorId")
    val vendorId: String,
    @SerialName("name")
    val name: String?
) : Selection {
    override var request: String
        get() = "REQUEST"
        set(value) {}
    override var post: String
        get() = "POST"
        set(value) {}
}

