package com.example.eitruck.ui.dash

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.example.eitruck.model.Units
import com.example.eitruck.model.generateColors
import com.example.eitruck.ui.filter.FilterDashDialog
import com.example.eitruck.ui.filter.FilterHomeDialog
import com.example.eitruck.ui.filter.FiltrosDashDisponiveis
import com.example.eitruck.ui.filter.FiltrosDisponiveis
import com.example.eitruck.ui.filter.SpinnerItem
import java.time.LocalDate
import kotlin.Boolean
import kotlin.text.ifEmpty

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

        segmentosDisponiveis = listOf("Todos")
        unidadesDisponiveis = listOf("Todos")
        regioesDisponiveis = listOf("Todos")

        setupLegendaRecycler()
        setupObservers()
        fetchData()

        viewModel.segments.observe(viewLifecycleOwner) { segments ->
            segmentosDisponiveis = if (segments.isEmpty()) {
                listOf("Todos")
            } else {
                listOf("Todos") + segments.map { it.nome }
            }
        }

        viewModel.units.observe(viewLifecycleOwner) { units ->
            unidadesDisponiveis = if (units.isEmpty()) {
                listOf("Todos")
            } else {
                listOf("Todos") + units.map { it.nome }
            }
        }

        viewModel.regions.observe(viewLifecycleOwner) { regions ->
            regioesDisponiveis = if (regions.isEmpty()) listOf("Todos") else listOf("Todos") + regions
        }


        val cargo = LoginSave(requireContext()).getPrefes().getString("user_cargo", "gerente_local")
        val strategy: FilterStrategy = when (cargo) {
            "Administrador" -> AnalyticsManager()
            "Gerente de Análise" -> AnalyticsManager()
            "Analista Regional" -> RegionsManager()
            "Analista Segmento" -> SegmentsManager()
            "Analista Local" -> LocalManager()
            else -> LocalManager()
        }

        binding.buttonFilterDash.setOnClickListener {
            val filtros = FiltrosDashDisponiveis(
                regioes = listOf("Todos") + (viewModel.regions.value ?: emptyList()),
                segmentos = listOf(SpinnerItem("", "Todos")) + (viewModel.segments.value?.map {
                    SpinnerItem(it.id.toString(), it.nome)
                } ?: emptyList()),
                unidades = listOf(SpinnerItem("", "Todos")) + (viewModel.units.value?.map {
                    SpinnerItem(it.id.toString(), it.nome)
                } ?: emptyList()),
                meses = listOf(
                    SpinnerItem("", "Todos"),
                    SpinnerItem("1", "Janeiro"),
                    SpinnerItem("2", "Fevereiro"),
                    SpinnerItem("3", "Março"),
                    SpinnerItem("4", "Abril"),
                    SpinnerItem("5", "Maio"),
                    SpinnerItem("6", "Junho"),
                    SpinnerItem("7", "Julho"),
                    SpinnerItem("8", "Agosto"),
                    SpinnerItem("9", "Setembro"),
                    SpinnerItem("10", "Outubro"),
                    SpinnerItem("11", "Novembro"),
                    SpinnerItem("12", "Dezembro")
                ),
                anos = listOf(SpinnerItem("", "Todos")) + (2021..2025).map { SpinnerItem(it.toString(), it.toString()) }
            )

            FilterDashDialog(
                context = requireContext(),
                filtrosDisponiveis = filtros,
                regiaoSelecionada = viewModel.regiao.ifEmpty { "Todos" },
                segmentoSelecionado = viewModel.segmento.toString().ifEmpty { "Todos" },
                unidadeSelecionada = viewModel.unidade.toString().ifEmpty { "Todos" },
                mesSelecionado = viewModel.mes.ifEmpty { "Todos" },
                anoSelecionado = viewModel.ano.ifEmpty { "Todos" },
                showRegiao = strategy.showRegiao,
                showSegmento = strategy.showSegmento,
                showUnidade = strategy.showUnidade,
                showMes = true,
                showAno = true
            ) { regiao, segmento, unidade, mes, ano ->
                viewModel.regiao = regiao ?: ""
                viewModel.segmento = segmento?.toIntOrNull() ?: 0
                viewModel.unidade = unidade?.toIntOrNull() ?: 0
                viewModel.mes = mes ?: ""
                viewModel.ano = ano ?: ""

                pagina = 1
                viewModel.filtrarDash()
            }.show()

            Log.d("FILTRO", viewModel.regiao)
            Log.d("FILTRO", viewModel.segmento.toString())
            Log.d("FILTRO", viewModel.unidade.toString())
        }
    }

    private fun setupLegendaRecycler() {
        binding.recyclerLegenda.layoutManager = LinearLayoutManager(requireContext())
        legendaAdapter = LegendaAdapter(emptyList())
        binding.recyclerLegenda.adapter = legendaAdapter
    }

    private fun setupObservers() {
        viewModel.carregandoLiveData.observe(viewLifecycleOwner) { carregando ->
            binding.progressBar2.visibility = if (carregando) View.VISIBLE else View.GONE
        }

        viewModel.dashOcorrenciaTipo.observe(viewLifecycleOwner) { lista ->
            if (!lista.isNullOrEmpty()) {
                val listaColorida = generateColors(lista)
                legendaAdapter.updateData(listaColorida)
                DashGraph(binding.barChart, listaColorida, requireContext())
            } else {
                binding.barChart.clear()
            }
        }

        viewModel.dashOcorrenciaGravidade.observe(viewLifecycleOwner) { lista ->
            // CORREÇÃO: Removemos a filtragem de mes/ano, pois a lista 'lista'
            // já vem filtrada por REGIAO, SEGMENTO, UNIDADE, MES e ANO pelo ViewModel.
            val listaParaSoma = lista.orEmpty()

            val agrupado = listaParaSoma.groupBy { it.gravidade }.mapValues { entry ->
                entry.value.sumOf { it.total_ocorrencias }
            }

            binding.numTotalLeve.text = (agrupado["Leve"] ?: 0).toString()
            binding.numTotalMedia.text = (agrupado["Média"] ?: agrupado["Media"] ?: 0).toString()
            binding.numTotalGraves.text = (agrupado["Grave"] ?: 0).toString()
            binding.numTotalGravissima.text = (agrupado["Gravíssima"] ?: 0).toString()

            Log.d("Filtrado gravidade", agrupado.toString())
        }

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

        viewModel.dashTotalOcorrencias.observe(viewLifecycleOwner) { lista ->
            val agora = LocalDate.now()
            val mesAtual = agora.monthValue
            val anoAtual = agora.year

            val ocorrenciasMesAtual = lista?.filter { it.mes == mesAtual && it.ano == anoAtual }.orEmpty()
            val totalOcorrencias = ocorrenciasMesAtual.sumOf { it.total_ocorrencias }
            binding.numTotalInfra.text = totalOcorrencias.toString()
        }

        viewModel.dashMotoristaInfra.observe(viewLifecycleOwner) { lista ->
            val infracoesFiltradas = lista.orEmpty()

            if (infracoesFiltradas.isNotEmpty()) {
                val totalInfracoesFiltradas = infracoesFiltradas.sumOf { it.quantidade_infracoes }
                binding.motMaisInfraQuant.text = "com $totalInfracoesFiltradas infrações"

                val motoristaMaisInfra = infracoesFiltradas
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
        viewModel.getSegments()
        viewModel.getUnits()
        viewModel.getRegions()
    }
}
