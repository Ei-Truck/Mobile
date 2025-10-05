package com.example.eitruck.network.service

import com.example.eitruck.model.LoginRequest
import com.example.eitruck.model.LoginResponse
import com.example.eitruck.model.Travel
import com.example.eitruck.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @GET("/viagens")
    suspend fun getTravels(): List<Travel>

    @GET("/usuarios/{id}")
    suspend fun getUser(@Path("id") id: Int): User

    @POST("/login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

}