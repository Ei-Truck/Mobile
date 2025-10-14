package com.example.eitruck.data.remote.repository.postgres

import com.example.eitruck.model.User
import com.example.eitruck.data.remote.network.client.postgres.PostgresClient
import okhttp3.MultipartBody

class UserRepository(token: String) {

    private val api = PostgresClient(token).userService

    suspend fun getUser(id: Int): User {
        return api.getUser(id)
    }

    suspend fun uploadPhoto(id: Int, photo: MultipartBody.Part): User {
        return api.uploadPhoto(id, photo)
    }


}