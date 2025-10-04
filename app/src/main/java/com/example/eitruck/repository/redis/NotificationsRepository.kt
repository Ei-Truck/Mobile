package com.example.eitruck.repository.redis

import com.example.eitruck.model.NotificationRequest
import com.example.eitruck.model.NotificationResponse
import com.example.eitruck.network.RedisApiClient

class NotificationsRepository {

    private val api = RedisApiClient().api

    suspend fun getNotifications(id: String): List<NotificationResponse> {
        return api.getNotifications(id)
    }

    suspend fun createNotification(id: String, notificationRequest: NotificationRequest): NotificationResponse {
        return api.createNotification(id, notificationRequest)
    }



}