package com.example.eitruck.data.remote.network.client.redis

import com.example.eitruck.data.remote.network.service.redis.RedisService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

public class RedisApiClient {
    private val url = "https://api-redis-latest.onrender.com"

    val api: RedisService by lazy {
        Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RedisService::class.java)
    }
}