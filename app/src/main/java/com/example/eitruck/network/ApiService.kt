package com.example.eitruck.network

import com.example.eitruck.model.Travel
import retrofit2.http.GET

interface ApiService {

    @GET("/viagens")
    suspend fun getTravels(): List<Travel>

}