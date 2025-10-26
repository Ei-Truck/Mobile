package com.example.eitruck.data.remote.network.client.postgres

import com.example.eitruck.data.remote.network.service.postgres.AuthService
import com.example.eitruck.data.remote.network.service.postgres.DriverService
import com.example.eitruck.data.remote.network.service.postgres.TravelService
import com.example.eitruck.data.remote.network.service.postgres.UserService
import com.example.eitruck.data.remote.network.service.postgres.InfractionService
import com.example.eitruck.data.remote.network.service.postgres.RegionService
import com.example.eitruck.data.remote.network.service.postgres.SegmentsService
import com.example.eitruck.data.remote.network.service.postgres.UnitsService
import com.example.eitruck.network.ChatService
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ChatApiClient {

    private val baseUrl = "https://api-sql-qa.onrender.com"

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val requestBuilder: Request.Builder = chain.request().newBuilder()
            chain.proceed(requestBuilder.build())
        }
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val chatService: ChatService by lazy { retrofit.create(ChatService::class.java) }
}
