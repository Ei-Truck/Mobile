package com.example.eitruck.ui.dash

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.eitruck.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

class DashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_dash, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val barChart = view.findViewById<BarChart>(R.id.barChart)

        // Dados do gráfico
        val entries = listOf(
            30f, 80f, 70f, 60f, 70f, 90f, 100f,
            20f, 85f, 77f, 50f, 60f, 90f
        ).mapIndexed { i, value -> BarEntry(i.toFloat(), value) }

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

        val dataSet = BarDataSet(entries, "Total de Infrações").apply {
            setColors(colors)
        }

        barChart.data = BarData(dataSet)

        // Configuração da legenda
        val legendLabels = listOf(
            "Embriaguez ao Volante", "Veículo em Mau Estado", "Uso de Celular",
            "Transporte Irregular", "Estacionamento Proibido", "Avanço de Faixa",
            "Não uso de Cinto", "Mão fora do Volante", "Freiadas Bruscas", "Excesso de Velocidade"
        )

        val legendEntries = legendLabels.mapIndexed { i, label ->
            LegendEntry(label, Legend.LegendForm.CIRCLE, 10f, 2f, null, colors[i])
        }

        barChart.legend.apply {
            isEnabled = true
            verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            orientation = Legend.LegendOrientation.VERTICAL
            form = Legend.LegendForm.CIRCLE
            formSize = 10f
            textSize = 12f
            textColor = ContextCompat.getColor(requireContext(), R.color.textColorPrimary)
            xEntrySpace = 10f
            yEntrySpace = 8f
            formToTextSpace = 8f
            isWordWrapEnabled = true
            setDrawInside(false)
            setCustom(legendEntries)
        }

        // Configurações finais
        barChart.description.isEnabled = false
        barChart.animateY(1000)
        barChart.invalidate()
    }
}
