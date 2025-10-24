package com.example.eitruck.data.remote.repository.postgres

import com.example.eitruck.model.Travel
import com.example.eitruck.data.remote.network.client.postgres.PostgresClient
import com.example.eitruck.model.TravelInfo

class TravelRepository(token: String) {

    private val api = PostgresClient(token).travelService

    suspend fun getTravels(): List<Travel> {
        return api.getTravels()
    }

    suspend fun getTravelsInfo(): List<TravelInfo> {
        return api.getTravelsInfo()
    }
}