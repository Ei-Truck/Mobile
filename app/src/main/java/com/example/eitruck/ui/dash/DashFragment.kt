package com.example.eitruck.ui.dash

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eitruck.R
import com.example.eitruck.model.LegendaItem
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarEntry

class DashFragment : Fragment() {

    var regiao: String = ""
    var segmento: String = ""
    var unidade: String = ""

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

        botao_filtro.setOnClickListener {
            mostrarFiltroModal()
        }
    }

    fun mostrarFiltroModal() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.modal_filter)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setGravity(Gravity.CENTER)

        val spinnerRegiao = dialog.findViewById<Spinner>(R.id.regiao_drop)
        val spinnerUnidade = dialog.findViewById<Spinner>(R.id.unidade_drop)
        val spinnerSegmento = dialog.findViewById<Spinner>(R.id.segmento_drop)

        val btnFiltrar = dialog.findViewById<Button>(R.id.filtrar)

        val regioesFila = arrayOf("Todos", "Região 1", "Região 2")
        val segmentoFila = arrayOf("Todos", "Segmento 1", "Segmento 2")
        val unidadeFila = arrayOf("Todos", "Unidade 1", "Unidade 2")

        val adapterRegiao = ArrayAdapter(requireContext(), R.layout.spinner_item, regioesFila)
        adapterRegiao.setDropDownViewResource(R.layout.spinner_drop_item)
        spinnerRegiao.adapter = adapterRegiao
        spinnerRegiao.setSelection(if (regiao != "") regioesFila.indexOf(regiao) else 0)

        val adapterSegmento = ArrayAdapter(requireContext(), R.layout.spinner_item, segmentoFila)
        adapterSegmento.setDropDownViewResource(R.layout.spinner_drop_item)
        spinnerSegmento.adapter = adapterSegmento
        spinnerSegmento.setSelection(if (segmento != "") segmentoFila.indexOf(segmento) else 0)


        val adapterUnidade = ArrayAdapter(requireContext(), R.layout.spinner_item, unidadeFila)
        adapterUnidade.setDropDownViewResource(R.layout.spinner_drop_item)
        spinnerUnidade.adapter = adapterUnidade
        spinnerUnidade.setSelection(if (unidade != "") unidadeFila.indexOf(unidade) else 0)


        btnFiltrar.setOnClickListener {
            regiao = spinnerRegiao.selectedItem.toString()
            segmento = spinnerSegmento.selectedItem.toString()
            unidade= spinnerUnidade.selectedItem.toString()
            dialog.dismiss()
        }

        dialog.show()
    }
}
