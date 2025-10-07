package com.example.eitruck.data.remote.network.service

import com.example.eitruck.model.NotificationRequest
import com.example.eitruck.model.NotificationResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RedisService {

    @GET("/notifications/{id}")
    suspend fun getNotifications(@Path("id") id: String): List<NotificationResponse>

    @POST("/notifications/{id}")
    suspend fun createNotification(@Path("id") id: String, @Body notificationRequest: NotificationRequest): NotificationResponse

}