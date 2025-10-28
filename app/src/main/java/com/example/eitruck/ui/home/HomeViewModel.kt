package com.example.eitruck.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eitruck.data.remote.repository.postgres.DriverRepository
import com.example.eitruck.data.remote.repository.postgres.InfractionsRepository
import com.example.eitruck.data.remote.repository.postgres.RegionRepository
import com.example.eitruck.model.WeeklyReport
import com.example.eitruck.data.remote.repository.postgres.SegmentsRepository
import com.example.eitruck.data.remote.repository.postgres.UnitRepository
import com.example.eitruck.model.DriverMonthlyReport
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    var regiao = ""
    var segmento = ""
    var unidade = ""

    private var infractionRepository: InfractionsRepository? = null
    private var segmentsRepository: SegmentsRepository? = null
    private var unitRepository: UnitRepository? = null
    private var regionRepository: RegionRepository? = null
    private var driverRepository: DriverRepository? = null



    private val _infractions = MutableLiveData<List<WeeklyReport>>()
    val infractions: LiveData<List<WeeklyReport>> get() = _infractions

    private val _segments = MutableLiveData<List<String>>()
    val segments: LiveData<List<String>> get() = _segments

    private val _units = MutableLiveData<List<String>>()
    val units: LiveData<List<String>> get() = _units

    private val _regions = MutableLiveData<List<String>>()
    val regions: LiveData<List<String>> get() = _regions

    private val _drivers = MutableLiveData<List<DriverMonthlyReport>>()
    val drivers: LiveData<List<DriverMonthlyReport>> get() = _drivers

    private var allDrivers: List<DriverMonthlyReport> = emptyList()

    private val carregando = MutableLiveData<Boolean>()
    val carregandoLiveData: LiveData<Boolean> get() = carregando


    fun setToken(token: String) {
        infractionRepository = InfractionsRepository(token)
        segmentsRepository = SegmentsRepository(token)
        unitRepository = UnitRepository(token)
        regionRepository = RegionRepository(token)
        driverRepository = DriverRepository(token)
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

    fun getRegions() {
        carregando.value = true
        viewModelScope.launch {
            try {
                regionRepository?.let {
                    val response = it.getRegions()
                    val lista = response.map { it.nome }
                    _regions.value = lista
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                carregando.value = false
            }
        }
    }

    fun getDriverWeeklyReport() {
        carregando.value = true
        viewModelScope.launch {
            try {
                driverRepository?.let { repo ->
                    val response = repo.getDrivers()
                    allDrivers = response
                    filtrarDrivers()
                } ?: throw IllegalStateException("Token não definido!")
            } catch (e: Exception) {
                e.printStackTrace()
                _drivers.value = emptyList()
            } finally {
                carregando.value = false
            }
        }
    }

    fun filtrarDrivers() {
        val reg = if (regiao == "Todos" || regiao.isBlank()) "" else regiao
        val seg = if (segmento == "Todos" || segmento.isBlank()) "" else segmento
        val uni = if (unidade == "Todos" || unidade.isBlank()) "" else unidade

        val filtrados = allDrivers.filter { motorista ->
            (reg.isEmpty() || motorista.localidade.contains(reg, ignoreCase = true)) &&
                    (seg.isEmpty() || motorista.segmento.contains(seg, ignoreCase = true)) &&
                    (uni.isEmpty() || motorista.unidade.contains(uni, ignoreCase = true))
        }

        _drivers.postValue(filtrados)
    }
}
