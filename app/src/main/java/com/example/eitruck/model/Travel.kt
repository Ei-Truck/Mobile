package com.example.eitruck.model

import com.google.gson.annotations.SerializedName
import java.util.Date

class Travel (
    val id: Int,
    val caminhao: Truck,
    val dtHrInicio: String,
    val pontuacao: Int =600,
    @SerializedName("wasAnalyzed")
    val tratada: Boolean
)

data class Truck(
    val placa: String
)