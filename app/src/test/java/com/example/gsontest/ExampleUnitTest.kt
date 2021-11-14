package com.example.gsontest

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun checkCodeGen() {
        var jsonEncoder = GsonEncoder()

        // Product Query
        var productRequest = ProductRequestFactory().request(
            operationName = "ProductDetailPage",
            parameters = ProductRequestParameters("12345"),
            selections = mutableSetOf(ProductRequestParameters.Selection.all)
        )

        var productJsonData = jsonEncoder.encode(productRequest)
        println(productJsonData)


        var updateProductRequest = UpdateProductRequestFactory().request(
            operationName = "ProductDetailPage",
            parameters = UpdateProductRequestParameters().also {
                it.product = ProductNetworkModel(id = "12345", 100.0, null)
            },
            selections = mutableSetOf(UpdateProductRequestParameters.Selection.all)
        )

        var updateProduct = jsonEncoder.encode(updateProductRequest)
        println(updateProduct)

    }
}