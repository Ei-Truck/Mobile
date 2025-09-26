package com.example.eitruck.model

class TravelInfo (
    val id: Int,
    val placa: String,
    val starDate: String,
    val endDate: String,
    val km: Int,
    val segment: String,
    val motorista: List<Driver>,
    val infracoes: List<Infractions>,
    val midia: List<Midia>
)

data class Infractions(
    val id: Int,
    val quantidade: Int,
    val tipoInfracao: String
)

data class Driver(
    val id: Int,
    val nome: String,
    val risco: String,
    val horarioComeco: String,
    val horarioFim: String
)

data class Midia(
    val id: Int,
    val midia: String
)