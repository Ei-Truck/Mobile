package com.example.eitruck.model

data class Mensagem (
    val texto: String,
    val isSent: Boolean,
    var isLoading: Boolean
)