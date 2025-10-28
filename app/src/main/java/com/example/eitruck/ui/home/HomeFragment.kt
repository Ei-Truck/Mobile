package com.example.eitruck.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eitruck.R
import com.example.eitruck.data.local.LoginSave
import com.example.eitruck.databinding.FragmentHomeBinding
import com.example.eitruck.domain.AnalyticsManager
import com.example.eitruck.domain.FilterStrategy
import com.example.eitruck.domain.LocalManager
import com.example.eitruck.domain.RegionsManager
import com.example.eitruck.domain.SegmentsManager
import com.example.eitruck.model.DriverMonthlyReport
import com.example.eitruck.ui.filter.FilterHomeDialog
import com.example.eitruck.ui.filter.FiltrosDisponiveis
import com.example.eitruck.ui.login.Login
import com.example.eitruck.ui.main.Main
import com.github.mikephil.charting.charts.CombinedChart

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: HomeAdapter
    private val viewModel: HomeViewModel by activityViewModels()
    private var motoristas: List<DriverMonthlyReport> = emptyList()
    private val numPaginas = 5
    private var pagina = 1

    private var segmentosDisponiveis: List<String> = emptyList()
    private var unidadesDisponiveis: List<String> = emptyList()
    private var regioesDisponiveis: List<String> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val combinedChart = view.findViewById<CombinedChart>(R.id.graficoSemanal)

        val token = LoginSave(requireContext(), null).getToken()
        if (!token.isNullOrEmpty()) {
            viewModel.setToken(token)
        } else {
            startActivity(Intent(requireContext(), Login::class.java))
            requireActivity().finish()
        }

        if (viewModel.drivers.value.isNullOrEmpty() || viewModel.infractions.value.isNullOrEmpty()) {
            viewModel.getDriverWeeklyReport()
            viewModel.getWeeklyReport()
            viewModel.getSegments()
            viewModel.getUnits()
            viewModel.getRegions()
        }

        val recyclerView = binding.ranking
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.drivers.observe(viewLifecycleOwner) {
            motoristas = it
            atualizarPagina()
        }

        val botaoFiltro = view.findViewById<Button>(R.id.buttonFilterHome)

        var segmentosDisponiveis: List<String> = listOf("Todos")
        var unidadesDisponiveis: List<String> = listOf("Todos")
        var regioesDisponiveis: List<String> = listOf("Todos")


        if (viewModel.segments.value.isNullOrEmpty()) {
            viewModel.getSegments()
        }

        if (viewModel.units.value.isNullOrEmpty()) {
            viewModel.getUnits()
            viewModel.infractions.observe(viewLifecycleOwner) { infraction ->
                HomeGraph(combinedChart, infraction, requireContext())
            }

            viewModel.carregandoLiveData.observe(viewLifecycleOwner) { carregando ->
                (requireActivity() as Main).showLoading(carregando)
            }

            viewModel.segments.observe(viewLifecycleOwner) { segments ->
                segmentosDisponiveis =
                    if (segments.isEmpty()) listOf() else listOf("Todos") + segments
                atualizarEstadoBotaoFiltro()
            }

            viewModel.units.observe(viewLifecycleOwner) { units ->
                unidadesDisponiveis = if (units.isEmpty()) listOf() else listOf("Todos") + units
                atualizarEstadoBotaoFiltro()
            }

            viewModel.regions.observe(viewLifecycleOwner) { regions ->
                regioesDisponiveis = if (regions.isEmpty()) listOf() else listOf("Todos") + regions
                atualizarEstadoBotaoFiltro()
            }

            val cargo =
                LoginSave(requireContext()).getPrefes().getString("user_cargo", "gerente_local")

            val strategy: FilterStrategy = when (cargo) {
                "Gerente de Análise" -> AnalyticsManager()
                "Analista Regional" -> RegionsManager()
                "Analista Segmento" -> SegmentsManager()
                "Analista Local" -> LocalManager()
                else -> LocalManager()
            }

            binding.buttonFilterHome.isEnabled = false

            binding.buttonFilterHome.setOnClickListener {
                val filtros = FiltrosDisponiveis(
                    segmentos = segmentosDisponiveis,
                    regioes = regioesDisponiveis,
                    unidades = unidadesDisponiveis
                )

                FilterHomeDialog(
                    context = requireContext(),
                    filtrosDisponiveis = filtros,
                    regiaoSelecionada = viewModel.regiao.ifEmpty { "Todos" },
                    segmentoSelecionado = viewModel.segmento.ifEmpty { "Todos" },
                    unidadeSelecionada = viewModel.unidade.ifEmpty { "Todos" },
                    showRegiao = strategy.showRegiao,
                    showSegmento = strategy.showSegmento,
                    showUnidade = strategy.showUnidade
                ) { regiao, segmento, unidade ->
                    viewModel.regiao = regiao ?: "Todos"
                    viewModel.segmento = segmento ?: "Todos"
                    viewModel.unidade = unidade ?: "Todos"

                    pagina = 1
                    viewModel.filtrarDrivers()
                }.show()
            }

            binding.backButton.setOnClickListener {
                if (pagina > 1) {
                    pagina--
                    atualizarPagina()
                }
            }

            binding.nextButton.setOnClickListener {
                val totalPaginas = (motoristas.size + numPaginas - 1) / numPaginas
                if (pagina < totalPaginas) {
                    pagina++
                    atualizarPagina()
                }
            }
        }
    }

    private fun atualizarEstadoBotaoFiltro() {
        val pronto = segmentosDisponiveis.isNotEmpty() &&
                unidadesDisponiveis.isNotEmpty() &&
                regioesDisponiveis.isNotEmpty()
        binding.buttonFilterHome.isEnabled = pronto
    }


    private fun atualizarPagina() {
        val recyclerView = binding.ranking
        if (motoristas.isEmpty()) {
            if (!::adapter.isInitialized) {
                adapter = HomeAdapter(emptyList())
                recyclerView.adapter = adapter
            } else {
                adapter.updateData(emptyList())
            }

            binding.pagesNumber.text = "0/0"
            recyclerView.layoutParams.height = 0
            recyclerView.requestLayout()
            return
        }

        val totalPaginas = (motoristas.size + numPaginas - 1) / numPaginas
        if (pagina > totalPaginas) pagina = totalPaginas
        if (pagina < 1) pagina = 1

        val inicio = (pagina - 1) * numPaginas
        val fim = minOf(inicio + numPaginas, motoristas.size)
        val motoristasPagina = motoristas.subList(inicio, fim)

        if (!::adapter.isInitialized) {
            adapter = HomeAdapter(motoristasPagina)
            recyclerView.adapter = adapter
        } else {
            adapter.updateData(motoristasPagina)
        }

        binding.pagesNumber.text = "$pagina/$totalPaginas"
        expandirRecycler(recyclerView)
    }

    private fun expandirRecycler(recyclerView: RecyclerView) {
        recyclerView.post {
            val adapter = recyclerView.adapter ?: return@post
            var totalHeight = 0
            val widthSpec = View.MeasureSpec.makeMeasureSpec(recyclerView.width, View.MeasureSpec.EXACTLY)
            val heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)

            for (i in 0 until adapter.itemCount) {
                val holder = adapter.createViewHolder(recyclerView, adapter.getItemViewType(i))
                adapter.bindViewHolder(holder, i)
                holder.itemView.measure(widthSpec, heightSpec)
                totalHeight += holder.itemView.measuredHeight
            }

            recyclerView.layoutParams.height = totalHeight
            recyclerView.requestLayout()
        }
    }
}
