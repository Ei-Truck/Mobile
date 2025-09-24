package com.example.eitruck.ui.filter

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import com.example.eitruck.R

class FilterDashDialog(
    private val context: Context,
    private val filtrosDisponiveis: FiltrosDashDisponiveis,
    private val onFilterSelected: (regiao: String?, segmento: String?, unidade: String?, mes: String?, ano: String?) -> Unit
) {

    fun show() {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.modal_filter_dash)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setGravity(Gravity.CENTER)

        val layoutRegiao   = dialog.findViewById<View>(R.id.RegiãoDash)
        val layoutUnidade  = dialog.findViewById<View>(R.id.UnidadeDash)
        val layoutSegmento = dialog.findViewById<View>(R.id.SegmentoDash)
        val layoutMes = dialog.findViewById<View>(R.id.MesDash)
        val layoutAno = dialog.findViewById<View>(R.id.AnoDash)


        val spinnerRegiao   = dialog.findViewById<Spinner>(R.id.regiaoDash_drop)
        val spinnerUnidade  = dialog.findViewById<Spinner>(R.id.unidadeDash_drop)
        val spinnerSegmento = dialog.findViewById<Spinner>(R.id.segmentoDash_drop)
        val spinnerMes = dialog.findViewById<Spinner>(R.id.mesDash_drop)
        val spinnerAno = dialog.findViewById<Spinner>(R.id.anoDash_drop)
        val btnFiltrar      = dialog.findViewById<Button>(R.id.filtrarDash)

        if (filtrosDisponiveis.regioes.isNotEmpty()) {
            setupSpinner(spinnerRegiao, filtrosDisponiveis.regioes)
            layoutRegiao.visibility = View.VISIBLE
        } else {
            layoutRegiao.visibility = View.GONE
        }

        if (filtrosDisponiveis.unidades.isNotEmpty()) {
            setupSpinner(spinnerUnidade, filtrosDisponiveis.unidades)
            layoutUnidade.visibility = View.VISIBLE
        } else {
            layoutUnidade.visibility = View.GONE
        }

        if (filtrosDisponiveis.segmentos.isNotEmpty()) {
            setupSpinner(spinnerSegmento, filtrosDisponiveis.segmentos)
            layoutSegmento.visibility = View.VISIBLE
        } else {
            layoutSegmento.visibility = View.GONE
        }

        if (filtrosDisponiveis.mes.isNotEmpty()) {
            setupSpinner(spinnerMes, filtrosDisponiveis.mes)
            layoutMes.visibility = View.VISIBLE
        } else {
            layoutMes.visibility = View.GONE
        }

        if (filtrosDisponiveis.ano.isNotEmpty()) {
            setupSpinner(spinnerAno, filtrosDisponiveis.ano)
            layoutAno.visibility = View.VISIBLE
        } else {
            layoutAno.visibility = View.GONE
        }


        btnFiltrar.setOnClickListener {
            val regiao   = if (spinnerRegiao.visibility == View.VISIBLE) spinnerRegiao.selectedItem.toString() else null
            val unidade  = if (spinnerUnidade.visibility == View.VISIBLE) spinnerUnidade.selectedItem.toString() else null
            val segmento = if (spinnerSegmento.visibility == View.VISIBLE) spinnerSegmento.selectedItem.toString() else null
            val mes = if (spinnerMes.visibility == View.VISIBLE) spinnerMes.selectedItem.toString() else null
            val ano = if (spinnerAno.visibility == View.VISIBLE) spinnerAno.selectedItem.toString() else null

            onFilterSelected(regiao, segmento, unidade, mes, ano)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun setupSpinner(spinner: Spinner, items: List<String>) {
        val adapter = ArrayAdapter(context, R.layout.spinner_item, items)
        adapter.setDropDownViewResource(R.layout.spinner_drop_item)
        spinner.adapter = adapter
        spinner.setSelection(0) // default
    }
}

// Modelo que representa os filtros disponíveis (vem do back)
data class FiltrosDashDisponiveis(
    val regioes: List<String> = emptyList(),
    val segmentos: List<String> = emptyList(),
    val unidades: List<String> = emptyList(),
    val mes: List<String> = emptyList(),
    val ano: List<String> = emptyList()
)
