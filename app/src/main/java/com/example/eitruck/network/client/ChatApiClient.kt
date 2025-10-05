package com.example.eitruck.network.client

import com.example.eitruck.network.service.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ChatApiClient {

    val url = "http://127.0.0.1:5000/health"

    val api : ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

    }
}