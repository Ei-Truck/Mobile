package com.example.eitruck.data.remote.repository

import com.example.eitruck.data.remote.network.client.postgres.ChatApiClient
import com.example.eitruck.model.AnswerChatBot

class ChatRepository {

    val chatApiClient: ChatApiClient = ChatApiClient()

    suspend fun sendAsk(ask: AskChatBot): AnswerChatBot {
        chatApiClient.chatService.sendAsk(ask)
    }
}
