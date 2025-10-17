package com.example.eitruck.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eitruck.data.remote.repository.postgres.InfractionsRepository
import com.example.eitruck.model.WeeklyReport
import com.example.eitruck.data.remote.repository.postgres.SegmentsRepository
import com.example.eitruck.data.remote.repository.postgres.UnitRepository
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    var regiao = ""
    var segmento = ""
    var unidade = ""

    private var infractionRepository: InfractionsRepository? = null
    private var segmentsRepository: SegmentsRepository? = null
    private var unitRepository: UnitRepository? = null

    private val _infractions = MutableLiveData<List<WeeklyReport>>()
    val infractions: MutableLiveData<List<WeeklyReport>> get() = _infractions

    private val _segments = MutableLiveData<List<String>>()
    val segments: MutableLiveData<List<String>> get() = _segments

    private val _units = MutableLiveData<List<String>>()
    val units: MutableLiveData<List<String>> get() = _units

    private val carregando = MutableLiveData<Boolean>()
    val carregandoLiveData: MutableLiveData<Boolean> get() = carregando

    fun setToken(token: String) {
        infractionRepository = InfractionsRepository(token)
        segmentsRepository = SegmentsRepository(token)
        unitRepository = UnitRepository(token)
    }

    fun getWeeklyReport() {
        carregando.value = true
        viewModelScope.launch {
            try {
                infractionRepository?.let {
                    val response = it.getWeeklyReport()
                    _infractions.value = response
                } ?: throw IllegalStateException("Token não definido!")
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                carregando.value = false
            }
        }
    }

    fun getSegments() {
        carregando.value = true
        viewModelScope.launch {
            try {
                segmentsRepository?.let {
                    val response = it.getSegments()
                    val lista = response.map { it.nome }
                    _segments.value = lista
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                carregando.value = false
            }
        }
    }

    fun getUnits() {
        carregando.value = true
        viewModelScope.launch {
            try {
                unitRepository?.let {
                    val response = it.getUnit()
                    val lista = response.map { it.nome }
                    _units.value = lista
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                carregando.value = false
            }
        }
    }
}
