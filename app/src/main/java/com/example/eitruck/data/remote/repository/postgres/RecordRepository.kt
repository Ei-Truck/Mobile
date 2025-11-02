package com.example.eitruck.data.remote.repository.postgres

import com.example.eitruck.data.remote.network.client.postgres.PostgresClient
import com.example.eitruck.model.TratativaRequest
import com.example.eitruck.model.TratativaResponse
import com.example.eitruck.model.Travel

class RecordRepository(token: String) {

    private val api = PostgresClient(token).recordService

    suspend fun createRecord(request: TratativaRequest): TratativaResponse {
        return api.createTratativa(request)
    }

    suspend fun getAllRecords(): List<TratativaResponse> {
        return api.getAllTratativas()
    }

    suspend fun getRecordById(id: Int): TratativaResponse {
        return api.getTratativaById(id)
    }

    suspend fun updateRecord(id: Int, request: TratativaRequest): TratativaResponse {
        return api.updateTratativa(id, request)
    }

    suspend fun updateRecordByDriver(
        idViagem: Int,
        idMotorista: Int,
        tratativa: String
    ): TratativaResponse {
        val body = mapOf("tratativa" to tratativa, "transactionMade" to "Atualização via análise")
        return api.updateTratativaByDriver(idViagem, idMotorista, body)
    }
}