package com.example.eitruck.repository.postgres

import com.example.eitruck.model.User
import com.example.eitruck.network.client.RetroFitClient

class UserRepository(token: String) {

    private val api = RetroFitClient(token).api

    suspend fun getUser(id: Int): User {
        return api.getUser(id)
    }


}