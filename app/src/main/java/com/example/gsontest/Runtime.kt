package com.example.gsontest

import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.lang.StringBuilder
import kotlin.properties.Delegates


// @generated
// Do not edit this generated file

// MARK: - Interfaces

interface GraphQLRequestParameters : Encodable {

    var operationDefinitionFormat: String
    var requestType: GraphQLRequestType
    var fragmentKey: String

    fun fragmentString(sets: Set<GraphQLSelection>): String
}

interface GraphQLSelection {
    fun getString(): String
}

sealed class GraphQLRequestType(val rawType: String) {
    object query : GraphQLRequestType("query")
    object mutation : GraphQLRequestType("mutation")
}

interface GraphQLNetworkModel : Codable

open class GraphQLRequesting : Encodable {
    @SerializedName("operationName")
    @Expose
    lateinit var operationName: String

    @SerializedName("query")
    @Expose
    lateinit var operationDefinition: String

    @SerializedName("variables")
    @Expose
    lateinit var parameters: GraphQLRequestParameters

    lateinit var parametersString: String

    lateinit var requestType: GraphQLRequestType
}

interface GraphQLQueryRequestFactoring<T:GraphQLRequestParameters> {

    fun request(
        operationName: String,
        parameters: T,
        selections: Set<GraphQLSelection>
    ): GraphQLRequest<T>
}

// MARK: - Base class for all GraphQL request

open class GraphQLQueryRequestFactory<T:GraphQLRequestParameters> : GraphQLQueryRequestFactoring<T> {

    private val parametersEncoder = GsonEncoder()

    override fun request(
        operationName: String,
        parameters: T ,
        selections: Set<GraphQLSelection>
    ): GraphQLRequest<T> {
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

open class GraphQLRequest<T> : GraphQLRequesting {

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

    fun encode(encoder: Encoder) {
        var container = encoder.getContainerWithKeys()
        container.encode(operationName, CodingKeys.operationName.rawValue)
        container.encode(parametersString, CodingKeys.parameters.rawValue)
        when (requestType) {
            is GraphQLRequestType.query -> {
                container.encode(operationDefinition, GraphQLRequestType.query.rawType)
            }
            is GraphQLRequestType.mutation -> {
                container.encode(operationDefinition, GraphQLRequestType.mutation.rawType)
            }
        }

    }
}

// MARK: - Request Parameters implementation

open class ProductRequestParameters(@Expose val id: String?) : GraphQLRequestParameters {
    sealed class Selection(val rawType: String) : GraphQLSelection {
        object all : Selection("id\n  price\n originalPrice")

        override fun getString(): String {
            return this.rawType
        }
    }


    override var operationDefinitionFormat: String = """query (${"$"}id: String!) {
    product(id: ${"$"}id) {
      %1s
    }
  }

  %2s
  """

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


    override var requestType: GraphQLRequestType = GraphQLRequestType.query

}

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
    override var requestType: GraphQLRequestType = GraphQLRequestType.mutation

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

    @Expose
    lateinit var product: ProductNetworkModel

}

/*
struct VendorRequestParameters: GraphQLRequestParameters {
    enum Selection : String, GraphQLSelection {
        case rating
                case ratingCount
                case isOpen
    }

    lateinit var operationDefinitionFormat: String = """
  query ($id: String!) {
    product(id: $id) {
      %2$@
    }
  }

  %3$@
  """

    lateinit var id: String

    lateinit var requestType: GraphQLRequestType = .query

    lateinit var fragmentKey: String = "...VendorFragment"
    // All fields marked as required allTypes.json will be in the format
    // without the option to selectively query it or not
    func fragmentString (_ selections : Set < Selection >) -> String {
        """
    fragment ProductFragment on Product {
      id
      latitude
      longitude
      \(selections.reduce(into: "") { $0 += "  \($1.rawValue)\n" })
    }
    """
    }

    private enum CodingKeys: String, CodingKey {
        case id
    }
}
*/

// MARK: - Network Model
open class ProductNetworkModel(
    @Expose val id: String,
    @Expose val price:Double?,
    @Expose val originalPrice:Double?
) : GraphQLNetworkModel {
    override fun encode(): String {
        return Gson().toJson(this)
    }
}

// MARK: - Generated Factory Class

typealias ProductRequestFactory = GraphQLQueryRequestFactory<ProductRequestParameters>

typealias UpdateProductRequestFactory = GraphQLQueryRequestFactory<UpdateProductRequestParameters>

/*typealias VendorRequestFactory = GraphQLQueryRequestFactory<
        VendorRequestParameters
        >*/

// MARK: - Autogenerat code ends here, below are the example code on how to use the autogenerated code

// MARK: - Demo Usage Code

//lateinit var productJsonString = String(data: productJsonData, encoding:.utf8)!


// Product Update
/*lateinit var updateProductRequest = try !UpdateProductRequestFactory().request(
    operationName: "ProductDetailPage",
    parameters: .init(product: .init(id: "productId123", price: 100, originalPrice: nil)),
    selections: [.all]
    )

    lateinit var updateProductJsonData = try !jsonEncoder.encode(updateProductRequest)
        lateinit var updateProductJsonString =
            String(data: updateProductJsonData, encoding:.utf8)!

        print(updateProductJsonString)
        print("")

// Vendor Query
        lateinit var vendorRequest = try !VendorRequestFactory().request(
            operationName: "VendorListingPage",
            parameters: .init(id: "vendorId123"),
            selections: [.rating, .ratingCount]
            )

            lateinit var vendorJsonData = try !jsonEncoder.encode(vendorRequest)
                lateinit var vendorJsonString =
                    String(data: vendorJsonData, encoding:.utf8)!

                print(vendorJsonString)
                print("")*/