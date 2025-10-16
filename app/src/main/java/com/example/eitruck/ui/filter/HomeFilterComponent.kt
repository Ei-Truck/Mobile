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

class FilterHomeDialog(
    private val context: Context,
    private val filtrosDisponiveis: FiltrosDisponiveis,
    private val regiaoSelecionada: String? = null,
    private val segmentoSelecionado: String? = null,
    private val unidadeSelecionada: String? = null,
    private val onFilterSelected: (regiao: String?, segmento: String?, unidade: String?) -> Unit
)
 {

    fun show() {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.modal_filter_home)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setGravity(Gravity.CENTER)

        val layoutRegiao   = dialog.findViewById<View>(R.id.Região)
        val layoutUnidade  = dialog.findViewById<View>(R.id.Unidade)
        val layoutSegmento = dialog.findViewById<View>(R.id.Segmento)

        val spinnerRegiao   = dialog.findViewById<Spinner>(R.id.regiao_drop)
        val spinnerUnidade  = dialog.findViewById<Spinner>(R.id.unidade_drop)
        val spinnerSegmento = dialog.findViewById<Spinner>(R.id.segmento_drop)
        val btnFiltrar      = dialog.findViewById<Button>(R.id.filtrarHome)

        if (filtrosDisponiveis.regioes.isNotEmpty()) {
            setupSpinner(spinnerRegiao, filtrosDisponiveis.regioes, regiaoSelecionada)
            layoutRegiao.visibility = View.VISIBLE
        } else {
            layoutRegiao.visibility = View.GONE
        }

        if (filtrosDisponiveis.unidades.isNotEmpty()) {
            setupSpinner(spinnerUnidade, filtrosDisponiveis.unidades, unidadeSelecionada)
            layoutUnidade.visibility = View.VISIBLE
        } else {
            layoutUnidade.visibility = View.GONE
        }

        if (filtrosDisponiveis.segmentos.isNotEmpty()) {
            setupSpinner(spinnerSegmento, filtrosDisponiveis.segmentos, segmentoSelecionado)
            layoutSegmento.visibility = View.VISIBLE
        } else {
            layoutSegmento.visibility = View.GONE
        }

        btnFiltrar.setOnClickListener {
            val regiao   = if (spinnerRegiao.visibility == View.VISIBLE) spinnerRegiao.selectedItem.toString() else null
            val unidade  = if (spinnerUnidade.visibility == View.VISIBLE) spinnerUnidade.selectedItem.toString() else null
            val segmento = if (spinnerSegmento.visibility == View.VISIBLE) spinnerSegmento.selectedItem.toString() else null

            onFilterSelected(regiao, segmento, unidade)

            dialog.dismiss()
        }

        dialog.show()
    }

     private fun setupSpinner(spinner: Spinner, items: List<String>, selectedItem: String?) {
         val adapter = ArrayAdapter(context, R.layout.spinner_item, items)
         adapter.setDropDownViewResource(R.layout.spinner_drop_item)
         spinner.adapter = adapter

         val index = items.indexOf(selectedItem)
         spinner.setSelection(if (index != -1) index else 0)
     }
 }

// Modelo que representa os filtros disponíveis (vem do back)
data class FiltrosDisponiveis(
    val regioes: List<String> = emptyList(),
    val segmentos: List<String> = emptyList(),
    val unidades: List<String> = emptyList()
)
