package com.example.eitruck.model

data class TravelBasicVision(
    val idViagem: Int,
    val placaCaminhao: String,
    val dataInicioViagem: String,
    val dataFimViagem: String,
    val kmViagem: String,
    val segmento: String
)

data class TravelInfractionInfo(
    val idViagem: Int,
    val idMotorista: Int,
    val idUnidade: Int,
    val idLocalidade: Int,
    val tipoLeve: Int,
    val tipoMedia: Int,
    val tipoGrave: Int,
    val tipoGravissima: Int
)

data class TravelDriverBasicVision(
    val idViagem: Int,
    val idMotorista: Int,
    val idSegmento: Int,
    val segmento: String,
    val idUnidade: Int,
    val unidade: String,
    val idLocalidade: Int,
    val nomeMotorista: String,
    val riscoMotorista: String,
    val urlMidiaConcatenada: String,
    val urlFotoMotorista: String,
)

data class TravelDriverInfractions(
    val idMotorista: Int,
    val idViagem: Int,
    val quantidadeInfracoes: Int
)

data class TravelAnalyzeRequest(
    val wasAnalyzed: Boolean
)

data class TravelAnalysisStatus(
    val id: Int,
    val wasAnalyzed: Boolean
)
