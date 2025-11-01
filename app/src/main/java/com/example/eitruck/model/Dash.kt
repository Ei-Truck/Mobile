package com.example.eitruck.model

data class DashOcorrenciaGravidade (
    val total_ocorrencias: Int,
    val gravidade: String,
    val mes: Int,
    val ano: Int,
    val idUnidade: Int,
    val idSegmento: Int,
    val idLocalidade: Int,
    val ufEstado: String
)

data class DashVariacaoInfracoes (
    val mes: Int,
    val ano: Int,
    val infracoes_mes_atual: Int,
    val infracoes_mes_passado: Int,
    val variacao: Double?,
    val idUnidade: Int,
    val idSegmento: Int,
    val idLocalidade: Int,
    val ufEstado: String
)

data class DashOcorrenciaTotal (
    val total_ocorrencias: Int,
    val mes: Int,
    val ano: Int,
    val idUnidade: Int,
    val idSegmento: Int,
    val idLocalidade: Int,
    val ufEstado: String
)