package com.example.eitruck.repository.postgres

import com.example.eitruck.model.Travel
import com.example.eitruck.network.client.RetroFitClient

class TravelRepository(token: String) {

    private val api = RetroFitClient(token).api

    suspend fun getTravels(): List<Travel> {
        return api.getTravels()
    }


}