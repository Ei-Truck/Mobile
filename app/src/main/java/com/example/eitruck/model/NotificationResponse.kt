package com.example.eitruck.model

class NotificationResponse (
    val id: String,
    val title: String,
    val message: String,
    val createdAt: String
)

class NotificationRequest (
    val title: String,
    val message: String
)