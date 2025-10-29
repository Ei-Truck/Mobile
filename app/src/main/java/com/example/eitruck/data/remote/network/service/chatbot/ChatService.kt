package com.example.eitruck.network

import com.example.eitruck.model.AnswerChatBot
import com.example.eitruck.model.AskChatBot
import retrofit2.http.Body
import retrofit2.http.POST

interface ChatService {

    @POST("/chat")
    suspend fun sendAsk(@Body ask: AskChatBot): AnswerChatBot

}