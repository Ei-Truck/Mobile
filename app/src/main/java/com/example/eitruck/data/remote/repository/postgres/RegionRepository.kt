package com.example.eitruck.data.remote.repository.postgres

import com.example.eitruck.data.remote.network.client.postgres.PostgresClient
import com.example.eitruck.model.Region

class RegionRepository(token: String?) {

    val api = PostgresClient(token).regionService

    suspend fun getRegions(): List<Region> {
        return api.getRegions()
    }
}