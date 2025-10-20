package com.example.eitruck.data.remote.repository.postgres

import com.example.eitruck.data.remote.network.client.postgres.PostgresClient
import com.example.eitruck.model.DriverMonthlyReport

class DriverRepository(token: String?) {

    val api = PostgresClient(token).driverService

    suspend fun getDrivers(): List<DriverMonthlyReport> {
        return api.getDrivers()
    }

}
