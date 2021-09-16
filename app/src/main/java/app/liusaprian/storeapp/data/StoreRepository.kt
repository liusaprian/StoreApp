package app.liusaprian.storeapp.data

import app.liusaprian.storeapp.network.api
import app.liusaprian.storeapp.response.ProductResponseItem

class StoreRepository {
    private val service = api

    suspend fun getProducts(category: String): List<ProductResponseItem>  {
        return if(category != "") service.getProducts().filter { it.category == category }
        else service.getProducts()
    }

    suspend fun getCategories() = service.getCategories()
}