package com.example.eitruck.data.remote.repository.postgres

import com.example.eitruck.data.remote.network.client.postgres.PostgresClient
import com.example.eitruck.model.DashLegendaItem
import com.example.eitruck.model.DashOcorrenciaGravidade

class DashRepository(token: String) {

    private val api = PostgresClient(token).dashService

    suspend fun getInfractionsByType(): List<DashLegendaItem> {
        return api.getInfractionsByType()
    }

    suspend fun getInfractionsByGravity(): List<DashOcorrenciaGravidade> {
        return api.getInfractionsByGravity()
    }
}