package com.example.eitruck.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory

public class RedisApiClient {
    private val url = "https://api-sql-qa.onrender.com"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
