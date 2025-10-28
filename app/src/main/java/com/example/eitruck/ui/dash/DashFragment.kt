package com.example.eitruck.ui.dash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eitruck.R
import com.example.eitruck.data.local.LoginSave
import com.example.eitruck.databinding.FragmentDashBinding
import com.example.eitruck.domain.AnalyticsManager
import com.example.eitruck.domain.FilterStrategy
import com.example.eitruck.domain.LocalManager
import com.example.eitruck.domain.RegionsManager
import com.example.eitruck.domain.SegmentsManager
import com.example.eitruck.model.generateColors
import com.example.eitruck.ui.filter.FilterDashDialog
import com.example.eitruck.ui.filter.FilterHomeDialog
import com.example.eitruck.ui.filter.FiltrosDashDisponiveis
import com.example.eitruck.ui.filter.FiltrosDisponiveis
import com.github.mikephil.charting.data.BarEntry
import java.time.LocalDate

class DashFragment : Fragment() {

    private lateinit var binding: FragmentDashBinding
    private val viewModel: DashViewModel by viewModels()
    private lateinit var legendaAdapter: LegendaAdapter

    private var segmentosDisponiveis = emptyList<String>()
    private var regioesDisponiveis = emptyList<String>()
    private var unidadesDisponiveis = emptyList<String>()

    private var pagina = 1


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDashBinding.bind(view)

        val login = LoginSave(requireContext())
        val token = login.getToken().toString()
        viewModel.setToken(token)
        viewModel.setTokenInfra(token)
        viewModel.setTokenDriver(token)

        setupLegendaRecycler()
        setupObservers()
        fetchData()
    }

    private fun setupLegendaRecycler() {
        binding.recyclerLegenda.layoutManager = LinearLayoutManager(requireContext())
        legendaAdapter = LegendaAdapter(emptyList())
        binding.recyclerLegenda.adapter = legendaAdapter
    }

    private fun setupObservers() {
        // Loading
        viewModel.carregandoLiveData.observe(viewLifecycleOwner) { carregando ->
            binding.progressBar2.visibility = if (carregando) View.VISIBLE else View.GONE
        }

        // Infrações por tipo
        viewModel.dashOcorrenciaTipo.observe(viewLifecycleOwner) { lista ->
            if (!lista.isNullOrEmpty()) {
                val listaColorida = generateColors(lista)
                legendaAdapter.updateData(listaColorida)
                DashGraph(binding.barChart, listaColorida, requireContext())
            } else {
                binding.barChart.clear()
            }
        }

        // Infrações por gravidade
        viewModel.dashOcorrenciaGravidade.observe(viewLifecycleOwner) { lista ->
            val agora = LocalDate.now()
            val mesAtual = agora.monthValue
            val anoAtual = agora.year

            val listaAtual = lista?.filter { it.mes == mesAtual && it.ano == anoAtual }.orEmpty()
            val agrupado = listaAtual.groupBy { it.gravidade }.mapValues { entry ->
                entry.value.sumOf { it.total_ocorrencias }
            }

            binding.numTotalLeve.text = (agrupado["Leve"] ?: 0).toString()
            binding.numTotalMedia.text = (agrupado["Média"] ?: agrupado["Media"] ?: 0).toString()
            binding.numTotalGraves.text = (agrupado["Grave"] ?: 0).toString()
            binding.numTotalGravissima.text = (agrupado["Gravíssima"] ?: 0).toString()
        }

        // Variação mensal
        viewModel.dashVariacao.observe(viewLifecycleOwner) { lista ->
            val agora = LocalDate.now()
            val mesAtual = agora.monthValue
            val anoAtual = agora.year

            val itemAtual = lista?.find { it.mes == mesAtual && it.ano == anoAtual }
            val variacao = itemAtual?.variacao ?: 0.0

            val variacaoFormatada = String.format("%.1f%%", variacao)
            binding.variacao.apply {
                text = if (variacao >= 0) "+$variacaoFormatada" else variacaoFormatada
                setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        if (variacao >= 0) R.color.colorPrimary else android.R.color.holo_red_dark
                    )
                )
            }
        }

        // Total de ocorrências
        viewModel.dashTotalOcorrencias.observe(viewLifecycleOwner) { lista ->
            val agora = LocalDate.now()
            val mesAtual = agora.monthValue
            val anoAtual = agora.year

            val ocorrenciasMesAtual = lista?.filter { it.mes == mesAtual && it.ano == anoAtual }.orEmpty()
            val totalOcorrencias = ocorrenciasMesAtual.sumOf { it.total_ocorrencias }
            binding.numTotalInfra.text = totalOcorrencias.toString()
        }

        // Motorista com mais infrações
        viewModel.dashMotoristaInfra.observe(viewLifecycleOwner) { lista ->
            val agora = LocalDate.now()
            val mesAtual = agora.monthValue
            val anoAtual = agora.year

            val infracoesMesAtual = lista?.filter { it.mes == mesAtual && it.ano == anoAtual }.orEmpty()

            if (infracoesMesAtual.isNotEmpty()) {
                val totalInfracoesMes = infracoesMesAtual.sumOf { it.quantidade_infracoes }
                binding.motMaisInfraQuant.text = "com $totalInfracoesMes infrações"

                val motoristaMaisInfra = infracoesMesAtual
                    .groupBy { it.motorista }
                    .mapValues { it.value.sumOf { it.quantidade_infracoes } }
                    .maxByOrNull { it.value }?.key ?: "-"

                binding.motMaisInfra.text = motoristaMaisInfra
            } else {
                binding.motMaisInfraQuant.text = "0 infrações"
                binding.motMaisInfra.text = "-"
            }
        }
    }

    private fun fetchData() {
        viewModel.getInfractionsByType()
        viewModel.getInfractionsByGravity()
        viewModel.getVariation()
        viewModel.getTotal()
        viewModel.getMotoristaInfra()
    }
}
