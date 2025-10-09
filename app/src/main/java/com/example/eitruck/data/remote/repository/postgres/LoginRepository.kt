package com.example.eitruck.data.remote.repository.postgres

import com.example.eitruck.model.LoginRequest
import com.example.eitruck.model.LoginResponse
import com.example.eitruck.data.remote.network.client.postgres.PostgresClient
import retrofit2.HttpException

class LoginRepository {

    private val api = PostgresClient(null).authService

    suspend fun login(request: LoginRequest): LoginResponse? {
        return try {
            api.login(request)
        } catch (e: HttpException) {
            if (e.code() == 500) {
                null
            } else {
                throw e
            }
        }
    }
}