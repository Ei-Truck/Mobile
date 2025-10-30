package com.example.eitruck.model

import com.example.eitruck.R

data class DriverMonthlyReport(
    var ranking_pontuacao: Int,
    val motorista: String,
    val id_unidade: Int,
    val unidade: String,
    val id_segmento: Int,
    val segmento: String,
    val id_localidade: String,
    val localidade_estado: String,
    val pontuacao_ultimo_mes: Int
)
 {
    val cor: Int
        get() = when (pontuacao_ultimo_mes) {
            in 0..250 -> R.color.colorTertiary
            in 251..500 -> R.color.colorSecondary
            in 501..750 -> R.color.colorPrimary
            else -> R.color.colorPrimaryDark
        }
}

data class DriverInfractions(
    val motorista: String,
    val quantidade_infracoes: Int,
    val mes: Int,
    val ano: Int,
)