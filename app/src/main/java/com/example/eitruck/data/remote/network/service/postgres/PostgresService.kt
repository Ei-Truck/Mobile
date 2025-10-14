package com.example.eitruck.data.remote.network.service.postgres

import com.example.eitruck.model.LoginRequest
import com.example.eitruck.model.LoginResponse
import com.example.eitruck.model.NotificationRequest
import com.example.eitruck.model.NotificationResponse
import com.example.eitruck.model.Travel
import com.example.eitruck.model.User
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path


interface AuthService {
    @POST("/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}

interface UserService {
    @GET("/usuarios/{id}")
    suspend fun getUser(@Path("id") id: Int): User

    @Multipart
    @POST("/usuarios/usuarios/{id}/foto")
    suspend fun uploadPhoto(@Path("id") id: Int, @Part photo: MultipartBody.Part): User
}

interface TravelService {
    @GET("/viagens/relatorio-simples")
    suspend fun getTravels(): List<Travel>
}

interface NotificationService {
    @POST("/notificacoes")
    suspend fun sendNotification(@Body request: NotificationRequest): NotificationResponse
}
