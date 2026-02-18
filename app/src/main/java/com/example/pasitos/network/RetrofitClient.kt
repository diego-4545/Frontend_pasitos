package com.example.pasitos.network

import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "https://backend-pasitos.onrender.com/"
    private const val API_KEY = "m802334711-5085abf5ad7f25fcb144e440"

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->

            val request: Request = chain.request().newBuilder()
                .addHeader("x-api-key", API_KEY)
                .build()

            chain.proceed(request)

        }
        .build()


    val instance: ApiService by lazy {

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

    }

}
