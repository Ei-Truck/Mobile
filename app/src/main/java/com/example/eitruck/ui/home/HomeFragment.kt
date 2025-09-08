package com.example.eitruck.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.eitruck.R
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val combinedChart = view.findViewById<CombinedChart>(R.id.graficoSemanal)

        val valores = arrayOf(7f, 15f, 12f, 11f, 20f)
        val labels = arrayOf("Segunda", "Terça", "Quarta", "Quinta", "Sexta")

        val barEntries = ArrayList<BarEntry>()
        val lineEntries = ArrayList<Entry>()

        for (i in valores.indices) {
            barEntries.add(BarEntry(i.toFloat(), valores[i]))
            lineEntries.add(Entry(i.toFloat(), valores[i]))
        }

        val barDataSet = BarDataSet(barEntries, "Vendas").apply {
            setColors(
                resources.getColor(R.color.colorPrimary),
                resources.getColor(R.color.colorSecondary),
                resources.getColor(R.color.colorTertiary),
                resources.getColor(R.color.colorPrimary),
                resources.getColor(R.color.colorPrimaryDark)
            )
            valueTextSize = 16f
            valueFormatter = object : ValueFormatter() {
                override fun getBarLabel(barEntry: BarEntry?): String {
                    return barEntry?.y?.toInt().toString()
                }
            }
        }

        val lineDataSet = LineDataSet(lineEntries, "Tendência").apply {
            color = resources.getColor(R.color.textColorSecondary)
            lineWidth = 2f

            setDrawCircles(true)
            circleRadius = 5f
            valueTextSize = 14f
            valueFormatter = object : ValueFormatter() {
                override fun getPointLabel(entry: Entry?): String {
                    return entry?.y?.toInt().toString()
                }
            }

            setCircleColor(resources.getColor(R.color.textColorSecondary))

            setDrawValues(false)
        }

        val combinedData = CombinedData().apply {
            setData(BarData(barDataSet))
            setData(LineData(lineDataSet))
        }


        combinedChart.data = combinedData

        val xAxis = combinedChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        xAxis.textSize = 16f
        xAxis.typeface = resources.getFont(R.font.texto_inter_regular)
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)
        xAxis.axisMinimum = -0.5f
        xAxis.axisMaximum = valores.size - 0.5f


        combinedChart.axisLeft.isEnabled = false
        combinedChart.axisRight.isEnabled = false
        combinedChart.setDrawGridBackground(false)
        combinedChart.setDrawBorders(false)
        combinedChart.setScaleEnabled(false)
        combinedChart.setBackgroundColor(resources.getColor(R.color.white))
        combinedChart.setExtraBottomOffset(15f)

        val barData = BarData(barDataSet)
        barData.barWidth = 0.8f
        combinedData.setData(barData)



        combinedChart.legend.isEnabled = false
        combinedChart.description.isEnabled = false

        barDataSet.setDrawValues(false)
        lineDataSet.setDrawValues(true)

        combinedChart.setDescription(combinedChart.description)


        combinedChart.invalidate()
    }
}
