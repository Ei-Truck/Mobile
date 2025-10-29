package com.example.eitruck.data.remote.network.client.postgres


import com.example.eitruck.network.ChatService
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ChatApiClient {

    private val baseUrl = "http://54.88.113.225:5000"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val chatService: ChatService by lazy { retrofit.create(ChatService::class.java) }
}
