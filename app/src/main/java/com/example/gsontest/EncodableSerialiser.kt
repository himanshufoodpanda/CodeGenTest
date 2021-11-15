package com.example.gsontest

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonObject


class EncodableSerialiser : KSerializer<Encodable> {
    override val descriptor: SerialDescriptor
        get() =  buildClassSerialDescriptor("animal")

    override fun deserialize(decoder: Decoder): Encodable {
        val input = decoder as? JsonDecoder ?: throw SerializationException("Expected JsonInput for ${decoder::class}")
        val jsonObject = input.decodeJsonElement() as? JsonObject ?: throw SerializationException("Expected JsonObject for ${input.decodeJsonElement()::class}")
        println("jsonObject = $jsonObject")
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun serialize(encoder: kotlinx.serialization.encoding.Encoder, value: Encodable) {
        when(value) {
            is GraphQLRequest -> {
                encoder.encodeSerializableValue(GraphQLRequest.serializer(),value)
            }
            is ProductRequestParameters -> value.id?.let { encoder.encodeSerializableValue(ProductRequestParameters.serializer(),value)}
            is UpdateProductRequestParameters -> value.product?.let { encoder.encodeSerializableValue(ProductNetworkModel.serializer(),value.product)}
            }
    }

}