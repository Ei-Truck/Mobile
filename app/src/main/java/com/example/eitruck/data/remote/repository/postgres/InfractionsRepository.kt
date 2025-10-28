package com.example.eitruck.data.remote.repository.postgres

import com.example.eitruck.data.remote.network.client.postgres.PostgresClient
import com.example.eitruck.model.DashOcorrenciaTotal
import com.example.eitruck.model.DashVariacaoInfracoes
import com.example.eitruck.model.WeeklyReport

class InfractionsRepository(token: String) {

    val api = PostgresClient(token).infractionsService

    suspend fun getWeeklyReport(): List<WeeklyReport> {
        return api.getInfractions()
    }

    suspend fun getVariation(): List<DashVariacaoInfracoes> {
        return api.getInfractionsVariation()
    }

    suspend fun getTotal(): List<DashOcorrenciaTotal> {
        return api.getInfractionsTotal()
    }
}