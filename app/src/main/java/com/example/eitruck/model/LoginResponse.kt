package com.example.eitruck.model

data class LoginResponse (
    val id: Int,
    val token: String,
    val email: String,
    val nomeCompleto: String,
    val telefone: String,
    val cargo: String
)

data class LoginRequest (
    val email: String,
    val senha: String
)