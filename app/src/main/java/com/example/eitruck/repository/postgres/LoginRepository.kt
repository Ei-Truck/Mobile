package com.example.eitruck.repository.postgres

import com.example.eitruck.model.LoginRequest
import com.example.eitruck.model.LoginResponse
import com.example.eitruck.model.User
import com.example.eitruck.network.client.RetroFitClient
import retrofit2.HttpException

class LoginRepository {

    private val api = RetroFitClient(null).api

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