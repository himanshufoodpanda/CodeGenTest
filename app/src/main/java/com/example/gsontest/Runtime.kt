package com.example.gsontest

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.json.Json
import java.lang.StringBuilder


// @generated
// Do not edit this generated file

// MARK: - Interfaces

interface GraphQLRequestParameters : Encodable {

    var operationDefinitionFormat: String
    var requestType: GraphQLRequestType
    var fragmentKey: String

    fun fragmentString(selections: Set<GraphQLSelection>): String
}

interface GraphQLSelection {
    fun getString(): String
}

sealed class GraphQLRequestType(val rawType: String) {
    object query : GraphQLRequestType("query")
    object mutation : GraphQLRequestType("mutation")
}

interface GraphQLNetworkModel : Codable

@Serializable
open class GraphQLRequesting : Encodable {
    @SerialName("operationName")
    lateinit var operationName: String

    @SerialName("query")
    lateinit var operationDefinition: String

    @SerialName("variables")
    lateinit var parameters: GraphQLRequestParameters

    @Transient
    lateinit var parametersString: String
    
    @Transient
    lateinit var requestType: GraphQLRequestType
}

interface GraphQLQueryRequestFactoring<T:GraphQLRequestParameters> {

    fun request(
        operationName: String,
        parameters: T,
        selections: Set<GraphQLSelection>
    ): GraphQLRequest
}

// MARK: - Base class for all GraphQL request

open class GraphQLQueryRequestFactory<T:GraphQLRequestParameters> : GraphQLQueryRequestFactoring<T> {

    private val parametersEncoder = GsonEncoder()

    override fun request(
        operationName: String,
        parameters: T ,
        selections: Set<GraphQLSelection>
    ): GraphQLRequest {
        var inlineFragment = parameters.fragmentKey
        var fragment = parameters.fragmentString(selections)

        var operationDefinition =
            String.format(format = parameters.operationDefinitionFormat, inlineFragment, fragment)

        var parametersData = parametersEncoder.encode(parameters)
        var parametersString: String? = parametersData

        return GraphQLRequest(
            operationName = operationName,
            operationDefinition = operationDefinition,
            parameters = parameters,
            parametersString = parametersString ?: "",
            requestType = parameters.requestType
        )
    }
}

// MARK: - GraphQL Request
@Serializable(with = GraphQLRequestSerialiser::class)
open class GraphQLRequest : GraphQLRequesting {

    sealed class CodingKeys(val rawValue: String) {
        object operationName : CodingKeys("operationName")
        object parameters : CodingKeys("variables")
        object query : CodingKeys("query")
        object mutation : CodingKeys("mutation")
    }

    constructor(
        operationName: String,
        operationDefinition: String,
        parameters: GraphQLRequestParameters,
        parametersString: String,
        requestType: GraphQLRequestType
    ) {
        this.operationName = operationName
        this.operationDefinition = operationDefinition
        this.parameters = parameters
        this.parametersString = parametersString
        this.requestType = requestType
    }

    fun tryEncoder(encoder:kotlinx.serialization.encoding.Encoder){
        var container = GsonEncoder().getContainerWithKeys()
        container.encode(operationName, CodingKeys.operationName.rawValue)
        container.encode(parametersString, CodingKeys.parameters.rawValue)
        container.encode(operationDefinition, requestType.rawType)
        encoder.encodeSerializableValue(MapSerializer(String.serializer(),String.serializer()),container.map)
    }

    fun encode(encoder: Encoder) {
        var container = encoder.getContainerWithKeys()
        container.encode(operationName, CodingKeys.operationName.rawValue)
        container.encode(parametersString, CodingKeys.parameters.rawValue)
        container.encode(operationDefinition, requestType.rawType)
    }
}

// MARK: - Request Parameters implementation

@Serializable
open class ProductRequestParameters(val id: String?) : GraphQLRequestParameters {
    sealed class Selection(val rawType: String) : GraphQLSelection {
        object all : Selection("id\n  price\n originalPrice")

        override fun getString(): String {
            return this.rawType
        }
    }

    @Transient
    override var operationDefinitionFormat: String = """query (${"$"}id: String!) {
    product(id: ${"$"}id) {
      %1s
    }
  }

  %2s
  """
    @Transient
    override var fragmentKey: String = "...ProductFragment"

    override fun fragmentString(selections: Set<GraphQLSelection>): String {

        val result = StringBuilder("")
        for (request in selections) {
            result.append("${request.getString()}\n")
        }

        return """
    fragment ProductFragment on Product {
      ${result.toString()}
    }
    """
    }

    @Transient
    override var requestType: GraphQLRequestType = GraphQLRequestType.query

}

@Serializable
open class UpdateProductRequestParameters() : GraphQLRequestParameters {
    sealed class Selection(val rawType: String) : GraphQLSelection {
        object all : Selection("id")

        override fun getString(): String {
            return this.rawType
        }
    }

    override var operationDefinitionFormat: String = """
     mutation (${"$"}product: Product!) {
     product(id: ${"$"}id) {
      %1s
    }
  }

  %2s
  """
    @Transient
    override var requestType: GraphQLRequestType = GraphQLRequestType.mutation

    @Transient
    override var fragmentKey: String = "...ProductFragment"

    override fun fragmentString (selections : Set <GraphQLSelection>):String
    {
        val result = StringBuilder("")
        for (request in selections) {
            result.append("${request.getString()}\n")
        }

      return  """
    fragment ProductFragment on Product {
      ${result.toString()}
    }
    """
    }

    @Transient
    lateinit var product: ProductNetworkModel
}

// MARK: - Network Model
@Serializable
open class ProductNetworkModel(
    val id: String,
    val price:Double?,
    val originalPrice:Double?
) : GraphQLNetworkModel {

    override fun encode(): String {
        return Json.encodeToString(serializer(),this)
    }
}

// MARK: - Generated Factory Class

typealias ProductRequestFactory = GraphQLQueryRequestFactory<ProductRequestParameters>

typealias UpdateProductRequestFactory = GraphQLQueryRequestFactory<UpdateProductRequestParameters>
