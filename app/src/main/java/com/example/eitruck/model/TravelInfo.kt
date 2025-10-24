package com.example.eitruck.model

data class TravelInfo (
    val idViagem: Int,
    val placaCaminhao: String,
    val dataInicioViagem: String,
    val dataFimViagem: String,
    val kmViagem: Int,
    val segmento: String,
    val unidade: String,
    val nomeMotorista: List<DriverTravelInfo>,
    val riscoMotorista: String,
    val urlMidiaConcatenada: String,
    val tipoGravidade: String,
    val tipoInfracao: String,
    val infracoes: List<InfractionsTravelInfo>
)

data class InfractionsTravelInfo(
    val id: Int,
    val quantidade: Int,
    val tipoInfracao: String
)

data class DriverTravelInfo(
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