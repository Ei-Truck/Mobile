package com.example.eitruck.api

import com.google.ai.client.generativeai.GenerativeModel

object GeminiService {
    private const val API_KEY = "AIzaSyCdWIopGGU8gM1Dpc0qs2bfpNPVjsMyflk"

    val model = GenerativeModel(
        modelName = "models/gemini-2.0-flash",
        apiKey = API_KEY
    )
}

