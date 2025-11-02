package com.example.eitruck.model

data class TratativaRequest(
    val idViagem: Int,
    val idMotorista: Int,
    val tratativa: String,
    val transactionMade: String
)

data class TratativaResponse(
    val id: Int,
    val idViagem: Int,
    val idMotorista: Int,
    val tratativa: String,
    val dtHrRegistro: String,
    val transactionMade: String,
    val updatedAt: String,
    val isInactive: Boolean
)
