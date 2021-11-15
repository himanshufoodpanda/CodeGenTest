package com.example.gsontest

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class GraphQLRequestSerialiser: KSerializer<GraphQLRequest> {
    override val descriptor: SerialDescriptor
        get() =  buildClassSerialDescriptor("animal")

    override fun deserialize(decoder: Decoder): GraphQLRequest {
        TODO("Not yet implemented")
    }

    override fun serialize(encoder: Encoder, value: GraphQLRequest) {
        value.tryEncoder(encoder)
    }
}