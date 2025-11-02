package com.example.eitruck.ui.dash

import PdfReportDashGenerator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eitruck.data.local.LoginSave
import com.example.eitruck.databinding.FragmentDashBinding
import com.example.eitruck.model.generateColors
import com.github.mikephil.charting.data.BarEntry
import java.time.LocalDate

class DashFragment : Fragment() {

    private lateinit var binding: FragmentDashBinding
    private val viewModel: DashViewModel by viewModels()
    private lateinit var legendaAdapter: LegendaAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val login = LoginSave(requireContext())
        viewModel.setToken(login.getToken().toString())

        binding.recyclerLegenda.layoutManager = LinearLayoutManager(requireContext())
        legendaAdapter = LegendaAdapter(emptyList())
        binding.recyclerLegenda.adapter = legendaAdapter

        binding.buttonGerarDash.setOnClickListener {
            PdfReportDashGenerator.gerarRelatorioDash(requireContext(),"Relatório", binding.pdfContentContainer,)
        }

//        grafico - infrações por tipo
        viewModel.dashOcorrenciaTipo.observe(viewLifecycleOwner) { lista ->
            if (!lista.isNullOrEmpty()) {
                val listaColorida = generateColors(lista)
                legendaAdapter.updateData(listaColorida)

                val entries = listaColorida.mapIndexed { index, item ->
                    BarEntry(index.toFloat(), item.total_ocorrencias.toFloat())
                }
                val labels = listaColorida.map { it.tipo_infracao }
                val cores = listaColorida.map { it.cor }

                DashGraph(binding.barChart, entries, labels, requireContext(), cores)
            } else {
                binding.barChart.clear()
            }
        }
        viewModel.getInfractionsByType()

//        infrações por gravidade
        viewModel.dashOcorrenciaGravidade.observe(viewLifecycleOwner) { lista ->
            if (!lista.isNullOrEmpty()) {
                val agora = LocalDate.now()
                val mesAtual = agora.monthValue
                val anoAtual = agora.year

                val listaAtual = lista.filter { it.mes == mesAtual && it.ano == anoAtual }

                val agrupado = listaAtual.groupBy { it.gravidade }.mapValues { entry ->
                    entry.value.sumOf { it.total_ocorrencias }
                }

                binding.numTotalLeve.text = (agrupado["Leve"] ?: 0).toString()
                binding.numTotalMedia.text = (agrupado["Média"] ?: agrupado["Media"] ?: 0).toString()
                binding.numTotalGraves.text = (agrupado["Grave"] ?: 0).toString()
                binding.numTotalGravissima.text = (agrupado["Gravíssima"] ?: 0).toString()
            } else {
                binding.numTotalLeve.text = "0"
                binding.numTotalMedia.text = "0"
                binding.numTotalGraves.text = "0"
                binding.numTotalGravissima.text = "0"
            }
        }
        viewModel.getInfractionsByGravity()
    }
}