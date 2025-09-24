package com.example.eitruck.ui.dash

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eitruck.R
import com.example.eitruck.model.LegendaItem
import com.example.eitruck.ui.filter.FilterDashDialog
import com.example.eitruck.ui.filter.FiltrosDashDisponiveis
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarEntry
import java.text.DateFormatSymbols
import java.util.Calendar
import java.util.Locale

class DashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_dash, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val barChart = view.findViewById<BarChart>(R.id.barChart)
        val recycler = view.findViewById<RecyclerView>(R.id.recyclerLegenda)
        recycler.layoutManager = LinearLayoutManager(requireContext())

        val lista = listOf(
            LegendaItem(Color.rgb(10, 64, 1), "Embriaguez ao Volante", 190, "33.6%"),
            LegendaItem(Color.rgb(90, 100, 238), "Veículo em Mau Estado", 170, "30.0%"),
            LegendaItem(Color.rgb(60, 166, 166), "Uso de Celular", 152, "26.9%"),
            LegendaItem(Color.rgb(182, 96, 29), "Transporte Irregular", 149, "26.3%"),
            LegendaItem(Color.rgb(231, 76, 60), "Estacionamento Proibido", 125, "22.1%"),
            LegendaItem(Color.rgb(52, 152, 219), "Avanço de Faixa", 116, "20.5%"),
            LegendaItem(Color.rgb(46, 204, 113), "Não uso de Cinto", 94, "16.6%")
        )

        recycler.adapter = LegendaAdapter(lista)

        val entries = lista.mapIndexed { index, item ->
            BarEntry(index.toFloat(), item.quantidade.toFloat())
        }
        val labels = lista.map { it.nome }

        DashGraph(barChart, entries, labels, requireContext())

        val botao_filtro = view.findViewById<Button>(R.id.buttonFilterDash)

        val meses = DateFormatSymbols(Locale("pt", "BR")).months
            .filter { it.isNotBlank() }
            .map { it.lowercase().replaceFirstChar { ch -> ch.uppercase() } }
            .toMutableList()
            .apply { add(0, "Mês Atual") }

        val anoAtual = Calendar.getInstance().get(Calendar.YEAR)
        val anosDesc = ((anoAtual) downTo (anoAtual - 5)).map { it.toString() }.toMutableList()


        val filtros = FiltrosDashDisponiveis(
            regioes = emptyList(),
            segmentos = listOf("Todos", "Segmento 1", "Segmento 2"),
            unidades = listOf("Todos", "Unidade 1", "Unidade 2"),
            mes = meses,
            ano = anosDesc
        )


        botao_filtro.setOnClickListener {
            FilterDashDialog(
                context = requireContext(),
                filtrosDisponiveis = filtros
            ) { regiao, segmento, unidade, mes, ano ->
                println("Filtro aplicado: $regiao | $segmento | $unidade | $mes | $ano")
            }.show()
        }
    }
}
