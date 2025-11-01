package com.example.eitruck.ui.dash

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.eitruck.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.example.eitruck.model.DashLegendaItem

class DashGraph(
    private val barChart: BarChart,
    lista: List<DashLegendaItem>,
    private val context: Context

) {
    init {
        if (lista.isEmpty()) {
            barChart.clear()
            barChart.setNoDataText("Nenhum dado disponível")
            barChart.setNoDataTextColor(ContextCompat.getColor(context, R.color.textColorPrimary))
            barChart.setNoDataTextTypeface(context.resources.getFont(R.font.texto_inter_regular))
            barChart.invalidate()
        } else {
            val entries = lista.mapIndexed { index, item ->
                BarEntry(index.toFloat(), item.total_ocorrencias.toFloat())
            }

            val labels = lista.map { it.tipo_infracao }

            val cores = lista.map { it.cor }

            val dataSet = BarDataSet(entries, "Infrações").apply {
                setColors(cores)
                setDrawValues(true)
            }

            val barData = BarData(dataSet).apply {
                barWidth = 0.7f
            }

            barChart.apply {
                data = barData
                setFitBars(true)
                axisRight.isEnabled = false
                axisLeft.isEnabled = true
                legend.isEnabled = false
                description.isEnabled = false

                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    granularity = 1f
                    valueFormatter = IndexAxisValueFormatter(labels)
                    setDrawLabels(true)
                }

                invalidate()
                notifyDataSetChanged()
            }
        }
    }
}
