package com.example.eitruck.ui.dash

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.example.eitruck.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter

class DashGraph(
    private val barChart: BarChart,
    private val entries: List<BarEntry>,
    private val labels: List<String>,
    private val context: Context
) {
    init {
        val colors = listOf(
            Color.rgb(60, 166, 166),   // ciano
            Color.rgb(90, 100, 238),   // azul
            Color.rgb(182, 96, 29),    // laranja
            Color.rgb(10, 64, 1),      // verde escuro
            Color.rgb(46, 204, 113),   // verde
            Color.rgb(241, 196, 15),   // amarelo
            Color.rgb(231, 76, 60),    // vermelho
            Color.rgb(52, 152, 219),   // azul claro
            Color.rgb(78, 37, 68),     // roxo escuro
            Color.rgb(127, 19, 41),    // vinho
            Color.rgb(138, 24, 89),    // rosa
            Color.rgb(85, 0, 146),     // roxo
            Color.rgb(225, 225, 225)   // cinza
        )

        val barDataSet = BarDataSet(entries, "Infrações").apply {
            setColors(colors)
            setDrawValues(false)
        }

        val barData = BarData(barDataSet).apply {
            barWidth = 0.7f
        }

        barChart.data = barData

        val xAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        xAxis.textSize = 10f
        xAxis.typeface = context.resources.getFont(R.font.texto_inter_regular)
        xAxis.setDrawGridLines(true)
        xAxis.setDrawAxisLine(true)
        xAxis.setDrawLabels(false)

        barChart.axisLeft.isEnabled = true
        barChart.axisRight.isEnabled = false
        barChart.axisLeft.setDrawGridLines(true)
        barChart.axisRight.setDrawGridLines(true)
        barChart.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        barChart.setExtraBottomOffset(15f)
        barChart.legend.isEnabled = false
        barChart.description.isEnabled = false
        barChart.setFitBars(true)

        barChart.invalidate()
    }
}
