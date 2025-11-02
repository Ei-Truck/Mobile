package com.example.eitruck.model

data class LegendaItem(
    val cor: Int,
    val nome: String,
    val quantidade: Int,
    val percentual: String
)
fun generateColors(size: Int): List<Int> {
    val colors = mutableListOf<Int>()
    val rnd = java.util.Random()
    repeat(size) {
        val color = android.graphics.Color.rgb(
            rnd.nextInt(256),
            rnd.nextInt(256),
            rnd.nextInt(256)
        )
        colors.add(color)
    }
    return colors
}
