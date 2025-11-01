package com.example.eitruck.ui.dash

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eitruck.data.remote.repository.postgres.DashRepository
import com.example.eitruck.data.remote.repository.postgres.DriverRepository
import com.example.eitruck.data.remote.repository.postgres.InfractionsRepository
import com.example.eitruck.data.remote.repository.postgres.RegionRepository
import com.example.eitruck.data.remote.repository.postgres.SegmentsRepository
import com.example.eitruck.data.remote.repository.postgres.UnitRepository
import com.example.eitruck.model.DashOcorrenciaGravidade
import com.example.eitruck.model.DashLegendaItem
import com.example.eitruck.model.DashOcorrenciaTotal
import com.example.eitruck.model.DashVariacaoInfracoes
import com.example.eitruck.model.DriverInfractions
import com.example.eitruck.model.Segments
import com.example.eitruck.model.Units
import kotlinx.coroutines.launch

class DashViewModel: ViewModel() {

    var regiao = ""
    var segmento = 0
    var unidade = 0
    var mes = ""
    var ano = ""

    private var dashRepository: DashRepository? = null
    private var infractionsRepository: InfractionsRepository? = null
    private var driverRepository: DriverRepository? = null
    private var regiaoRepository: RegionRepository? = null
    private var segmentoRepository: SegmentsRepository? = null
    private var unidadeRepository: UnitRepository? = null



    private val carregando = MutableLiveData<Boolean>()
    val carregandoLiveData: LiveData<Boolean> get() = carregando

    private val _dashOcorrenciaTipo = MutableLiveData<List<DashLegendaItem>>()
    val dashOcorrenciaTipo: LiveData<List<DashLegendaItem>> = _dashOcorrenciaTipo

    private val _dashOcorrenciaGravidade = MutableLiveData<List<DashOcorrenciaGravidade>>()
    val dashOcorrenciaGravidade: LiveData<List<DashOcorrenciaGravidade>> = _dashOcorrenciaGravidade

    private val _dashVariacao = MutableLiveData<List<DashVariacaoInfracoes>>()
    val dashVariacao: LiveData<List<DashVariacaoInfracoes>> = _dashVariacao

    private val _dashTotalOcorrencias = MutableLiveData<List<DashOcorrenciaTotal>>()
    val dashTotalOcorrencias: LiveData<List<DashOcorrenciaTotal>> = _dashTotalOcorrencias

    private val _dashMotoristaInfra = MutableLiveData<List<DriverInfractions>>()
    val dashMotoristaInfra: LiveData<List<DriverInfractions>> = _dashMotoristaInfra

    private val _regioesDisponiveis = MutableLiveData<List<String>>()
    var regions: LiveData<List<String>> = _regioesDisponiveis

    private val _segmentosDisponiveis = MutableLiveData<List<Segments>>()
    var segments: LiveData<List<Segments>> = _segmentosDisponiveis

    private val _unidadesDisponiveis = MutableLiveData<List<Units>>()
    var units: LiveData<List<Units>> = _unidadesDisponiveis


    private var allDashOcorrenciaTipo: List<DashLegendaItem> = emptyList()
    private var allDashOcorrenciaGravidade: List<DashOcorrenciaGravidade> = emptyList()
    private var allDashVariacao: List<DashVariacaoInfracoes> = emptyList()
    private var allDashTotalOcorrencias: List<DashOcorrenciaTotal> = emptyList()
    private var allDashMotoristaInfra: List<DriverInfractions> = emptyList()

    fun setToken(token: String){
        dashRepository = DashRepository(token)
        infractionsRepository = InfractionsRepository(token)
        driverRepository = DriverRepository(token)
        regiaoRepository = RegionRepository(token)
        segmentoRepository = SegmentsRepository(token)
        unidadeRepository = UnitRepository(token)
    }

    fun getInfractionsByType() {
        viewModelScope.launch {
            carregando.value = true
            try {
                dashRepository?.let{
                    val response = it.getInfractionsByType()
                    allDashOcorrenciaTipo = response
                    _dashOcorrenciaTipo.value = response
                } ?: throw IllegalStateException("Token não definido!")
            } catch (e: Exception){
                e.printStackTrace()
            }finally {
                carregando.value = false
            }
        }
    }

    fun getInfractionsByGravity() {
        viewModelScope.launch {
            carregando.value = true
            try {
                dashRepository?.let{
                    val response = it.getInfractionsByGravity()
                    allDashOcorrenciaGravidade = response
                    _dashOcorrenciaGravidade.value = response
                } ?: throw IllegalStateException("Token não definido!")
            } catch (e: Exception){
                e.printStackTrace()
            }finally {
                carregando.value = false
            }
        }
    }

    fun getVariation() {
        viewModelScope.launch {
            carregando.value = true
            try {
                infractionsRepository?.let{
                    val response = it.getVariation()
                    allDashVariacao = response
                    _dashVariacao.value = response
                } ?: throw IllegalStateException("Token não definido!")
            } catch (e: Exception){
                e.printStackTrace()
            }finally {
                carregando.value = false
            }
        }
    }

    fun getTotal() {
        viewModelScope.launch {
            carregando.value = true
            try {
                infractionsRepository?.let{
                    val response = it.getTotal()
                    allDashTotalOcorrencias = response
                    _dashTotalOcorrencias.value = response
                } ?: throw IllegalStateException("Token não definido!")
            } catch (e: Exception){
                e.printStackTrace()
            }finally {
                carregando.value = false
            }
        }
    }

    fun getMotoristaInfra() {
        viewModelScope.launch {
            carregando.value = true
            try {
                driverRepository?.let{
                    val response = it.getDriversInfractions()
                    allDashMotoristaInfra = response
                    _dashMotoristaInfra.value = response
                } ?: throw IllegalStateException("Token não definido!")
            } catch (e: Exception){
                e.printStackTrace()
            }finally {
                carregando.value = false
            }
        }
    }

    fun getRegions() {
        viewModelScope.launch {
            carregando.value = true
            try {
                regiaoRepository?.let{
                    val response = it.getRegions().map { it.ufEstado }
                    _regioesDisponiveis.value = response
                } ?: throw IllegalStateException("Token não definido!")
            } catch (e: Exception){
                e.printStackTrace()
            }finally {
                carregando.value = false
            }
        }
    }

    fun getSegments() {
        viewModelScope.launch {
            carregando.value = true
            try {
                segmentoRepository?.let{
                    val response = it.getSegments()
                    _segmentosDisponiveis.value = response
                    } ?: throw IllegalStateException("Token não definido!")
            } catch (e: Exception){
                e.printStackTrace()
            }finally {
                carregando.value = false
            }
        }
    }

    fun getUnits() {
        viewModelScope.launch {
            carregando.value = true
            try {
                unidadeRepository?.let{
                    val response = it.getUnit()
                    _unidadesDisponiveis.value = response
                    } ?: throw IllegalStateException("Token não definido!")
            } catch (e: Exception){
                e.printStackTrace()
            }
            finally {
                carregando.value = false
            }
        }
    }


    fun filtrarDash() {
        val regFilter: String? = regiao.takeIf { it.isNotBlank() }
        val segFilter: Int? = segmento.takeIf { it != 0 }
        val uniFilter: Int? = unidade.takeIf { it != 0 }
        val mesFiltro = mes.toIntOrNull()
        val anoFiltro = ano.toIntOrNull()

         Log.d("FILTRAR_DEBUG", "reg=$regFilter seg=$segFilter uni=$uniFilter mes=$mesFiltro ano=$anoFiltro")

        _dashOcorrenciaTipo.value = allDashOcorrenciaTipo.filter { item ->
            val regOk = regFilter == null || item.ufEstado.equals(regFilter, ignoreCase = true)
            val segOk = segFilter == null || item.idSegmento == segFilter
            val uniOk = uniFilter == null || item.idUnidade == uniFilter
            val mesOk = mesFiltro == null || item.mes == mesFiltro
            val anoOk = anoFiltro == null || item.ano == anoFiltro

            regOk && segOk && uniOk && mesOk && anoOk
        }

        _dashOcorrenciaGravidade.value = allDashOcorrenciaGravidade.filter { item ->
            val regOk = regFilter == null || item.ufEstado.equals(regFilter, ignoreCase = true)
            val segOk = segFilter == null || item.idSegmento == segFilter
            val uniOk = uniFilter == null || item.idUnidade == uniFilter
            val mesOk = mesFiltro == null || item.mes == mesFiltro
            val anoOk = anoFiltro == null || item.ano == anoFiltro

            regOk && segOk && uniOk && mesOk && anoOk
        }

        _dashVariacao.value = allDashVariacao.filter { item ->
            val regOk = regFilter == null || item.ufEstado.equals(regFilter, ignoreCase = true)
            val segOk = segFilter == null || item.idSegmento == segFilter
            val uniOk = uniFilter == null || item.idUnidade == uniFilter
            val mesOk = mesFiltro == null || item.mes == mesFiltro
            val anoOk = anoFiltro == null || item.ano == anoFiltro

            regOk && segOk && uniOk && mesOk && anoOk
        }

        _dashTotalOcorrencias.value = allDashTotalOcorrencias.filter { item ->
            val regOk = regFilter == null || item.ufEstado.equals(regFilter, ignoreCase = true)
            val segOk = segFilter == null || item.idSegmento == segFilter
            val uniOk = uniFilter == null || item.idUnidade == uniFilter
            val mesOk = mesFiltro == null || item.mes == mesFiltro
            val anoOk = anoFiltro == null || item.ano == anoFiltro

            regOk && segOk && uniOk && mesOk && anoOk
        }

        _dashMotoristaInfra.value = allDashMotoristaInfra.filter { item ->
            val regOk = regFilter == null || item.ufEstado.equals(regFilter, ignoreCase = true)
            val segOk = segFilter == null || item.idSegmento == segFilter
            val uniOk = uniFilter == null || item.idUnidade == uniFilter
            val mesOk = mesFiltro == null || item.mes == mesFiltro
            val anoOk = anoFiltro == null || item.ano == anoFiltro

            regOk && segOk && uniOk && mesOk && anoOk
        }
    }


}