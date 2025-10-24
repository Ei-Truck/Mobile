package com.example.eitruck.model

import android.graphics.Color

data class LegendaItem(
    var cor: Int = 0,
    val tipo_infracao: String,
    val total_ocorrencias: Int,
    val porcentagem_do_total: String
)

fun generateColors(lista: List<LegendaItem>): List<LegendaItem> {
    if (lista.isEmpty()) return emptyList()

    val total = lista.size

    return lista.mapIndexed { index, item ->
        val hue = ((index.toFloat() / total.toFloat()) * 720f) % 360f
        val saturation = 1f
        val value = 1f

        val color = Color.HSVToColor(floatArrayOf(hue, saturation, value))
        item.copy(cor = color)
    }
}

