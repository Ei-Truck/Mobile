package com.example.eitruck.ui.dash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eitruck.data.remote.repository.postgres.DashRepository
import com.example.eitruck.model.DashOcorrenciaGravidade
import com.example.eitruck.model.LegendaItem
import kotlinx.coroutines.launch

class DashViewModel: ViewModel() {

    private var repository: DashRepository? = null

    private val _dashOcorrenciaTipo = MutableLiveData<List<LegendaItem>>()
    val dashOcorrenciaTipo: LiveData<List<LegendaItem>> = _dashOcorrenciaTipo

    private val _dashOcorrenciaGravidade = MutableLiveData<List<DashOcorrenciaGravidade>>()
    val dashOcorrenciaGravidade: LiveData<List<DashOcorrenciaGravidade>> = _dashOcorrenciaGravidade

    fun setToken(token: String){
        repository = DashRepository(token)
    }

    fun getInfractionsByType() {
        viewModelScope.launch {
            try {
                repository?.let{
                    val response = it.getInfractionsByType()
                    _dashOcorrenciaTipo.value = response
                } ?: throw IllegalStateException("Token não definido!")
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun getInfractionsByGravity() {
        viewModelScope.launch {
            try {
                repository?.let{
                    val response = it.getInfractionsByGravity()
                    _dashOcorrenciaGravidade.value = response
                } ?: throw IllegalStateException("Token não definido!")
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}