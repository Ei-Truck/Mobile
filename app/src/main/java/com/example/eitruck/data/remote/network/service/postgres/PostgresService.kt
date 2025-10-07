package com.example.eitruck.data.remote.network.service.postgres

import com.example.eitruck.model.LoginRequest
import com.example.eitruck.model.LoginResponse
import com.example.eitruck.model.NotificationRequest
import com.example.eitruck.model.NotificationResponse
import com.example.eitruck.model.Travel
import com.example.eitruck.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface AuthService {
    @POST("/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}

interface UserService {
    @GET("/usuarios/{id}")
    suspend fun getUser(@Path("id") id: Int): User
}

interface TravelService {
    @GET("/viagens")
    suspend fun getTravels(): List<Travel>
}

interface NotificationService {
    @POST("/notificacoes")
    suspend fun sendNotification(@Body request: NotificationRequest): NotificationResponse
}
