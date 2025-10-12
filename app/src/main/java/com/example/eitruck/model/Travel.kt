package com.example.eitruck.model

import com.google.gson.annotations.SerializedName

class Travel (
    val id_viagem: Int,
    val placa_caminhao: String,
    val data_inicio_viagem: String,
    val pontuacao_total: Int,
    @SerializedName("wasAnalyzed")
    val tratada: Boolean = true
)
