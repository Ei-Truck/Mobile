package com.example.eitruck.data.remote.repository.postgres

import com.example.eitruck.model.Travel
import com.example.eitruck.data.remote.network.client.postgres.PostgresClient
import com.example.eitruck.model.TravelAnalysisStatus
import com.example.eitruck.model.TravelAnalyzeRequest
import com.example.eitruck.model.TravelBasicVision
import com.example.eitruck.model.TravelDriverBasicVision
import com.example.eitruck.model.TravelDriverInfractions
import com.example.eitruck.model.TravelInfractionInfo

class TravelRepository(token: String) {

    private val api = PostgresClient(token).travelService

    suspend fun getTravels(): List<Travel> {
        return api.getTravels()
    }

    suspend fun updateTravelStatus(id: Int, request: TravelAnalyzeRequest): List<TravelInfractionInfo> {
        return api.updateTravelStatus(id, request)
    }

    suspend fun getTravelsInfo(id: Int): TravelBasicVision {
        return api.getTravelsInfo(id)
    }

    suspend fun getDriversInfo(id: Int): List<TravelDriverBasicVision> {
        return api.getDriversInfo(id)
    }

    suspend fun getDriversInfractions(id: Int): List<TravelDriverInfractions> {
        return api.getDriversInfractions(id)
    }

    suspend fun getInfractionsByGravity(id: Int): List<TravelInfractionInfo> {
        return api.getInfractionsByGravity(id)
    }

    suspend fun getTravelAnalysisStatus(id: Int): TravelAnalysisStatus {
        return api.getTravelAnalysisStatus(id)
    }
}
