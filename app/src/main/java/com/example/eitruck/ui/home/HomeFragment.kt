package com.example.eitruck.ui.home

import MotoristaRanking
import android.app.Dialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eitruck.R
import com.example.eitruck.databinding.FragmentHomeBinding
import com.github.mikephil.charting.charts.CombinedChart
import com.google.android.material.bottomsheet.BottomSheetDialog


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: HomeAdapter

    var regiao: String = ""
    var segmento: String = ""
    var unidade: String = ""

    private val listaCompleta: List<MotoristaRanking> = listOf(
        MotoristaRanking(1, "Motorista fuiweiovjewiojewiogj", 1000),
        MotoristaRanking(2, "Motorista 2", 750),
        MotoristaRanking(3, "Motorista 3", 500),
        MotoristaRanking(4, "Motorista 4", 250),
        MotoristaRanking(5, "Motorista 5", 100),
        MotoristaRanking(6, "Motorista 6", 100),
        MotoristaRanking(7, "Motorista 7", 100),
        MotoristaRanking(8, "Motorista 8", 100),
        MotoristaRanking(9, "Motorista 9", 100),
        MotoristaRanking(10, "Motorista 10", 100),
        MotoristaRanking(11, "Motorista 11", 100)


    )

    private val totalPages =  (listaCompleta.size + 5 - 1) / 5

    private var currentPage = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val combinedChart = view.findViewById<CombinedChart>(R.id.graficoSemanal)

        val valores = arrayOf(7f, 15f, 12f, 11f, 20f)
        val labels = arrayOf("Segunda", "Terça", "Quarta", "Quinta", "Sexta")

        HomeGraph(combinedChart, valores, labels, requireContext())

        val recyclerView = view.findViewById<RecyclerView>(R.id.ranking)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = HomeAdapter(emptyList())
        recyclerView.adapter = adapter
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.post {
            recyclerView.expand()
        }

        loadPage(currentPage)

        binding.backButton.setOnClickListener {
            if (currentPage > 1) {
                currentPage--
                loadPage(currentPage)
            }
        }

        binding.nextButton.setOnClickListener {
            if (currentPage < totalPages){
                currentPage++
                loadPage(currentPage)
            }
        }

        val botao_filtro = view.findViewById<Button>(R.id.filtro_botao)

        botao_filtro.setOnClickListener {
            mostrarFiltroModal()
        }



    }

    fun RecyclerView.expand() {
        val adapter = adapter ?: return
        var totalHeight = 0
        for (i in 0 until adapter.itemCount) {
            val holder = adapter.createViewHolder(this, adapter.getItemViewType(i))
            adapter.onBindViewHolder(holder, i)
            holder.itemView.measure(
                View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.UNSPECIFIED
            )
            totalHeight += holder.itemView.measuredHeight
        }
        layoutParams.height = totalHeight
        requestLayout()
    }

    fun loadPage(page: Int){
        val pageSize = 5
        val startIndex = (page - 1) * pageSize
        val endIndex = minOf(startIndex + pageSize, listaCompleta.size)

        if (startIndex >= listaCompleta.size) {
            return
        }

        val subList = listaCompleta.subList(startIndex, endIndex)

        adapter.updateData(subList)
        binding.pagesNumber.text = "$page/${(listaCompleta.size + pageSize - 1)/pageSize}"

        mudarBotao(page)
    }

    fun mudarBotao(page: Int){
        if (currentPage==totalPages){
            binding.nextButton.iconTint = ColorStateList(arrayOf(intArrayOf()), intArrayOf(ContextCompat.getColor(requireContext(), R.color.textColorSecondary)))
        } else {
            binding.nextButton.iconTint = ColorStateList(arrayOf(intArrayOf()), intArrayOf(ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark)))
        }

        if (currentPage==1){
            binding.backButton.iconTint = ColorStateList(arrayOf(intArrayOf()), intArrayOf(ContextCompat.getColor(requireContext(), R.color.textColorSecondary)))
        } else {
            binding.backButton.iconTint = ColorStateList(arrayOf(intArrayOf()), intArrayOf(ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark)))
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
