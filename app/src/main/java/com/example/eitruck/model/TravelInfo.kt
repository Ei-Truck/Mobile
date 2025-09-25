package com.example.eitruck.model

class TravelInfo (
    val id: Int,
    val infracoes: List<Infractions>
)

data class Infractions(
    val id: Int,
    val quantidade: Int,
    val tipoInfracao: String
)