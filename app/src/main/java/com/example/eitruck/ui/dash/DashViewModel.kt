package com.example.eitruck.ui.dash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eitruck.data.remote.repository.postgres.DashRepository
import com.example.eitruck.data.remote.repository.postgres.DriverRepository
import com.example.eitruck.data.remote.repository.postgres.InfractionsRepository
import com.example.eitruck.model.DashOcorrenciaGravidade
import com.example.eitruck.model.DashLegendaItem
import com.example.eitruck.model.DashOcorrenciaTotal
import com.example.eitruck.model.DashVariacaoInfracoes
import com.example.eitruck.model.DriverInfractions
import kotlinx.coroutines.launch

class DashViewModel: ViewModel() {

    private var dashRepository: DashRepository? = null
    private var infractionsRepository: InfractionsRepository? = null
    private var driverRepository: DriverRepository? = null


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


    fun setToken(token: String){
        dashRepository = DashRepository(token)
    }
    fun setTokenInfra(token: String){
        infractionsRepository = InfractionsRepository(token)
    }
    fun setTokenDriver(token: String){
        driverRepository = DriverRepository(token)
    }

    fun getInfractionsByType() {
        viewModelScope.launch {
            carregando.value = true
            try {
                dashRepository?.let{
                    val response = it.getInfractionsByType()
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
                    _dashMotoristaInfra.value = response
                } ?: throw IllegalStateException("Token não definido!")
            } catch (e: Exception){
                e.printStackTrace()
            }finally {
                carregando.value = false
            }
        }
    }
}