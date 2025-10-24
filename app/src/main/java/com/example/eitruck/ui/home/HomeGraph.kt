package com.example.eitruck.ui.home

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.eitruck.R
import com.example.eitruck.model.WeeklyReport
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter

class HomeGraph(
    private val combinedChart: CombinedChart,
    private val valores: List<WeeklyReport>,
    private val context: Context
) {

    init {
        if (valores.isEmpty()) {
            combinedChart.clear()
            combinedChart.setNoDataText("Nenhum dado disponível")
            combinedChart.setNoDataTextColor(ContextCompat.getColor(context, R.color.textColorPrimary))
            combinedChart.setNoDataTextTypeface(context.resources.getFont(R.font.texto_inter_regular))
            combinedChart.invalidate()
        } else {
            configurarGrafico()
        }
    }

    private fun configurarGrafico() {
        val labels = valores.map { it.diasemana }

        val barEntries = ArrayList<BarEntry>()
        val lineEntries = ArrayList<Entry>()

        for (i in valores.indices) {
            val yValue = valores[i].total_infracoes.toFloat()
            barEntries.add(BarEntry(i.toFloat(), yValue))
            lineEntries.add(Entry(i.toFloat(), yValue))
        }

        val cores = listOf(
            ContextCompat.getColor(context, R.color.colorPrimaryDark),
            ContextCompat.getColor(context, R.color.colorSecondary),
            ContextCompat.getColor(context, R.color.colorPrimaryDark),
            ContextCompat.getColor(context, R.color.colorTertiary),
        )

        val barDataSet = BarDataSet(barEntries, "Infrações").apply {
            colors = cores
            valueTextSize = 10f
            setDrawValues(false)
        }

        val lineDataSet = LineDataSet(lineEntries, "Tendência").apply {
            color = ContextCompat.getColor(context, R.color.textColorSecondary)
            lineWidth = 2f
            setDrawCircles(true)
            circleRadius = 4f
            setCircleColor(ContextCompat.getColor(context, R.color.textColorSecondary))
            valueTextSize = 9f
            setDrawValues(true)
            valueFormatter = object : ValueFormatter() {
                override fun getPointLabel(entry: Entry?): String {
                    return entry?.y?.toInt().toString()
                }
            }
        }

        val barData = BarData(barDataSet).apply {
            barWidth = calcularBarWidth(valores.size)
        }

        val lineData = LineData(lineDataSet)

        val combinedData = CombinedData().apply {
            setData(barData)
            setData(lineData)
        }

        val xAxis = combinedChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        xAxis.textSize = 10f
        xAxis.typeface = context.resources.getFont(R.font.texto_inter_regular)
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)
        xAxis.axisMinimum = -0.5f
        xAxis.axisMaximum = valores.size - 0.5f
        xAxis.labelRotationAngle = -15f

        combinedChart.apply {
            data = combinedData
            axisRight.isEnabled = false
            setDrawGridBackground(false)
            setDrawBorders(false)
            setScaleEnabled(false)
            isDragEnabled = false
            setBackgroundColor(ContextCompat.getColor(context, R.color.white))
            setExtraBottomOffset(10f)
            legend.isEnabled = false
            description.isEnabled = false
            animateY(700)
            invalidate()
        }
    }

    private fun calcularBarWidth(itemCount: Int): Float {
        return when {
            itemCount <= 5 -> 0.7f
            itemCount <= 7 -> 0.6f
            itemCount <= 10 -> 0.5f
            else -> 0.4f
        }
    }
}
