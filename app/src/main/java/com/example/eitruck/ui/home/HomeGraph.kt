package com.example.eitruck.ui.home

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.eitruck.R
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter

class HomeGraph(
    private val combinedChart: CombinedChart,
    private val valores: Array<Float>,
    private val labels: Array<String>,
    private val context: Context
) {

    init {
        val barEntries = ArrayList<BarEntry>()
        val lineEntries = ArrayList<Entry>()

        for (i in valores.indices) {
            barEntries.add(BarEntry(i.toFloat(), valores[i]))
            lineEntries.add(Entry(i.toFloat(), valores[i]))
        }

        val barDataSet = BarDataSet(barEntries, "Vendas").apply {
            setColors(
                ContextCompat.getColor(context, R.color.colorPrimary),
                ContextCompat.getColor(context, R.color.colorSecondary),
                ContextCompat.getColor(context, R.color.colorTertiary),
                ContextCompat.getColor(context, R.color.colorPrimary),
                ContextCompat.getColor(context, R.color.colorPrimaryDark)
            )
            valueTextSize = 16f
            valueFormatter = object : ValueFormatter() {
                override fun getBarLabel(barEntry: BarEntry?): String {
                    return barEntry?.y?.toInt().toString()
                }
            }
            setDrawValues(false)
        }

        val lineDataSet = LineDataSet(lineEntries, "Tendência").apply {
            color = ContextCompat.getColor(context, R.color.textColorSecondary)
            lineWidth = 2f
            setDrawCircles(true)
            circleRadius = 5f
            setCircleColor(ContextCompat.getColor(context, R.color.textColorSecondary))
            valueTextSize = 14f
            setDrawValues(true)
            valueFormatter = object : ValueFormatter() {
                override fun getPointLabel(entry: Entry?): String {
                    return entry?.y?.toInt().toString()
                }
            }
        }

        val combinedData = CombinedData().apply {
            setData(BarData(barDataSet).apply { barWidth = 0.8f })
            setData(LineData(lineDataSet))
        }

        val xAxis = combinedChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        xAxis.textSize = 16f
        xAxis.typeface = context.resources.getFont(R.font.texto_inter_regular)
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)
        xAxis.axisMinimum = -0.5f
        xAxis.axisMaximum = valores.size - 0.5f

        combinedChart.data = combinedData
        combinedChart.axisLeft.isEnabled = false
        combinedChart.axisRight.isEnabled = false
        combinedChart.setDrawGridBackground(false)
        combinedChart.setDrawBorders(false)
        combinedChart.setScaleEnabled(false)
        combinedChart.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        combinedChart.setExtraBottomOffset(15f)
        combinedChart.legend.isEnabled = false
        combinedChart.description.isEnabled = false

        combinedChart.invalidate()
    }
}
