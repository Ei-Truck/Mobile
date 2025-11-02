package com.example.eitruck.model

data class TravelResponse(
    val id: Int,
    val dtHrInicio: String,
    val dtHrFim: String,
    val kmViagem: String,
    val wasAnalyzed: Boolean,
    val caminhao: Caminhao,
    val usuario: Usuario,
    val origem: Localidade,
    val destino: Localidade
)

data class Caminhao(
    val id: Int,
    val chassi: String,
    val placa: String,
    val modelo: String
)

data class Usuario(
    val id: Int,
    val nomeCompleto: String,
    val email: String
)

data class Localidade(
    val id: Int,
    val cidade: String,
    val ufEstado: String
)
