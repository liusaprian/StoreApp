package app.liusaprian.storeapp.network

import app.liusaprian.storeapp.response.ProductResponseItem
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

interface ApiService {

    @GET("products")
    suspend fun getProducts() : List<ProductResponseItem>

    @GET("products/categories")
    suspend fun getCategories() : List<String>
}

val api: ApiService by lazy {
    Retrofit.Builder()
        .baseUrl("https://fakestoreapi.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(
            OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build()
        )
        .build()
        .create(ApiService::class.java)
}