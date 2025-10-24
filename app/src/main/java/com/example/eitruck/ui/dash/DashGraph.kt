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

class DashGraph(
    private val barChart: BarChart,
    private val entries: List<BarEntry>,
    private val labels: List<String>,
    private val context: Context,
    private val cores: List<Int>
) {
    init {
        if (entries.isNotEmpty() && labels.isNotEmpty()) {
            val barDataSet = BarDataSet(entries, "Infrações").apply {
                colors = cores
                setDrawValues(true)
                valueTextSize = 10f
                valueTextColor = Color.BLACK
            }

            val barData = BarData(barDataSet).apply {
                barWidth = 0.7f
            }

            barChart.apply {
                data = barData

                setBackgroundColor(ContextCompat.getColor(context, R.color.white))
                setDrawGridBackground(false)
                setFitBars(true)
                setExtraOffsets(5f, 10f, 5f, 10f)
                description.isEnabled = false
                legend.isEnabled = false
                animateY(1000)

                axisLeft.apply {
                    axisMinimum = 0f
                    setDrawGridLines(true)
                    textColor = Color.DKGRAY
                    textSize = 10f
                }

                axisRight.isEnabled = false

                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    granularity = 1f
                    valueFormatter = IndexAxisValueFormatter(labels)
                    textColor = Color.DKGRAY
                    textSize = 10f
                    setDrawGridLines(false)
                    setDrawAxisLine(true)
                    setDrawLabels(true)
                }

                invalidate()
            }
        } else {
            barChart.clear()
            barChart.invalidate()
        }
    }
}

