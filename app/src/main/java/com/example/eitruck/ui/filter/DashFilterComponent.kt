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
import android.widget.AdapterView


class FilterDashDialog(
    private val context: Context,
    private val filtrosDisponiveis: FiltrosDashDisponiveis,
    private val regiaoSelecionada: String? = null,
    private val segmentoSelecionado: String? = null,
    private val unidadeSelecionada: String? = null,
    private val mesSelecionado: String? = null,
    private val anoSelecionado: String? = null,
    private val showRegiao: Boolean = true,
    private val showSegmento: Boolean = true,
    private val showUnidade: Boolean = true,
    private val showMes: Boolean = true,
    private val showAno: Boolean = true,
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
        val layoutMes      = dialog.findViewById<View>(R.id.MesDash)
        val layoutAno      = dialog.findViewById<View>(R.id.AnoDash)

        val spinnerRegiao   = dialog.findViewById<Spinner>(R.id.regiaoDash_drop)
        val spinnerUnidade  = dialog.findViewById<Spinner>(R.id.unidadeDash_drop)
        val spinnerSegmento = dialog.findViewById<Spinner>(R.id.segmentoDash_drop)
        val spinnerMes      = dialog.findViewById<Spinner>(R.id.mesDash_drop)
        val spinnerAno      = dialog.findViewById<Spinner>(R.id.anoDash_drop)
        val btnFiltrar      = dialog.findViewById<Button>(R.id.filtrarDash)

        layoutRegiao.visibility = if (showRegiao && filtrosDisponiveis.regioes.isNotEmpty()) View.VISIBLE else View.GONE
        layoutSegmento.visibility = if (showSegmento && filtrosDisponiveis.segmentos.isNotEmpty()) View.VISIBLE else View.GONE
        layoutUnidade.visibility = if (showUnidade && filtrosDisponiveis.unidades.isNotEmpty()) View.VISIBLE else View.GONE
        layoutMes.visibility = if (showMes && filtrosDisponiveis.meses.isNotEmpty()) View.VISIBLE else View.GONE
        layoutAno.visibility = if (showAno && filtrosDisponiveis.anos.isNotEmpty()) View.VISIBLE else View.GONE

        if (layoutRegiao.visibility == View.VISIBLE)
            setupSpinner(spinnerRegiao, filtrosDisponiveis.regioes, regiaoSelecionada)

        var segmentoSelecionadoId: String? = null
        if (layoutSegmento.visibility == View.VISIBLE) {
            val nomesSegmentos = filtrosDisponiveis.segmentos.map { it.nome }
            val idsSegmentos = filtrosDisponiveis.segmentos.map { it.id }

            val segmentoAdapter = ArrayAdapter(context, R.layout.spinner_item, nomesSegmentos)
            segmentoAdapter.setDropDownViewResource(R.layout.spinner_drop_item)
            spinnerSegmento.adapter = segmentoAdapter

            val index = nomesSegmentos.indexOf(
                filtrosDisponiveis.segmentos.find { it.id == segmentoSelecionado }?.nome
            )
            spinnerSegmento.setSelection(if (index != -1) index else 0)

            spinnerSegmento.setOnItemSelectedListener(SimpleItemSelectedListener { position ->
                segmentoSelecionadoId = idsSegmentos[position]
            })
        }

        var unidadeSelecionadoId: String? = null
        if (layoutUnidade.visibility == View.VISIBLE) {
            val nomesUnidades = filtrosDisponiveis.unidades.map { it.nome }
            val idsUnidades = filtrosDisponiveis.unidades.map { it.id }

            val unidadeAdapter = ArrayAdapter(context, R.layout.spinner_item, nomesUnidades)
            unidadeAdapter.setDropDownViewResource(R.layout.spinner_drop_item)
            spinnerUnidade.adapter = unidadeAdapter

            val index = nomesUnidades.indexOf(
                filtrosDisponiveis.unidades.find { it.id == unidadeSelecionada }?.nome
            )
            spinnerUnidade.setSelection(if (index != -1) index else 0)

            spinnerUnidade.setOnItemSelectedListener(SimpleItemSelectedListener { position ->
                unidadeSelecionadoId = idsUnidades[position]
            })
        }


        var mesSelecionadoId: String? = null
        if (layoutMes.visibility == View.VISIBLE) {
            val nomesMeses = filtrosDisponiveis.meses.map { it.nome }
            val idsMeses = filtrosDisponiveis.meses.map { it.id }

            val mesAdapter = ArrayAdapter(context, R.layout.spinner_item, nomesMeses)
            mesAdapter.setDropDownViewResource(R.layout.spinner_drop_item)
            spinnerMes.adapter = mesAdapter

            val index = nomesMeses.indexOf(
                filtrosDisponiveis.meses.find { it.id == mesSelecionado }?.nome
            )
            spinnerMes.setSelection(if (index != -1) index else 0)

            spinnerMes.setOnItemSelectedListener(SimpleItemSelectedListener { position ->
                mesSelecionadoId = idsMeses[position]
            })
        }

        var anoSelecionadoId: String? = null
        if (layoutAno.visibility == View.VISIBLE) {
            val nomesAnos = filtrosDisponiveis.anos.map { it.nome }
            val idsAnos = filtrosDisponiveis.anos.map { it.id }

            val anoAdapter = ArrayAdapter(context, R.layout.spinner_item, nomesAnos)
            anoAdapter.setDropDownViewResource(R.layout.spinner_drop_item)
            spinnerAno.adapter = anoAdapter

            val index = nomesAnos.indexOf(
                filtrosDisponiveis.anos.find { it.id == anoSelecionado }?.nome
            )
            spinnerAno.setSelection(if (index != -1) index else 0)

            spinnerAno.setOnItemSelectedListener(SimpleItemSelectedListener { position ->
                anoSelecionadoId = idsAnos[position]
            })
        }

        btnFiltrar.setOnClickListener {
            val regiao = if (spinnerRegiao.selectedItem?.toString() == "Todos") "" else spinnerRegiao.selectedItem?.toString()
            val segmento = segmentoSelecionadoId
            val unidade  = unidadeSelecionadoId
            val mes      = mesSelecionadoId
            val ano      = anoSelecionadoId

            onFilterSelected(regiao, segmento, unidade, mes, ano)
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

class SimpleItemSelectedListener(private val onSelect: (Int) -> Unit) : AdapterView.OnItemSelectedListener {
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) = onSelect(position)
    override fun onNothingSelected(parent: AdapterView<*>?) {}
}

data class SpinnerItem(val id: String, val nome: String)

data class FiltrosDashDisponiveis(
    val regioes: List<String> = emptyList(),
    val segmentos: List<SpinnerItem> = emptyList(),
    val unidades: List<SpinnerItem> = emptyList(),
    val meses: List<SpinnerItem> = emptyList(),
    val anos: List<SpinnerItem> = emptyList()
)
