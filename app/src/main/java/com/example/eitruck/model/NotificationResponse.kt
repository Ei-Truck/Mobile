package com.example.eitruck.model

data class NotificationResponse (
    val id: String,
    val title: String,
    val message: String,
    val createdAt: String
)

data class NotificationRequest (
    val title: String,
    val message: String
)