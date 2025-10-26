package com.example.eitruck.model

import com.google.firebase.Timestamp

class AskChatBot (
    val user_id: Int,
    val session_id: Int,
    val question: String
)

class AnswerChatBot (
    val content: ContentChatBot,
    val timestamp: Timestamp
)

class ContentChatBot (
    val aswer: String
)