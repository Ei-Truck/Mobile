package com.example.eitruck.repository

import com.example.eitruck.model.Travel
import com.example.eitruck.network.RetroFitClient

class TravelRepository {

    private val api = RetroFitClient().api

    suspend fun getTravels(): List<Travel> {
        return api.getTravels()
    }

}