package com.example.eitruck.data.remote.repository.postgres

import com.example.eitruck.data.remote.network.client.postgres.PostgresClient
import com.example.eitruck.model.Units

class UnitRepository(token: String?) {

    val api = PostgresClient(token).unitsService

    suspend fun getUnit(): List<Units> {
        return api.getUnits()
    }
}