package com.example.eitruck.ui.travel_info

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eitruck.data.remote.repository.postgres.TravelRepository
import com.example.eitruck.model.TravelAnalyzeRequest
import com.example.eitruck.model.TravelBasicVision
import com.example.eitruck.model.TravelDriverBasicVision
import com.example.eitruck.model.TravelDriverInfractions
import com.example.eitruck.model.TravelInfractionInfo
import kotlinx.coroutines.launch


class TravelInfoViewModel: ViewModel() {

    private var repository: TravelRepository? = null

    private val carregando = MutableLiveData<Boolean>()
    val carregandoLiveData: LiveData<Boolean> get() = carregando

    private val _updatetravel = MutableLiveData<List<TravelInfractionInfo>>()
    val updatetravel: LiveData<List<TravelInfractionInfo>> = _updatetravel

    private val _viagemAnalisada = MutableLiveData<Boolean>()
    val viagemAnalisada: LiveData<Boolean> get() = _viagemAnalisada

    private val _travelInfo = MutableLiveData<TravelBasicVision>()
    val travelInfo: LiveData<TravelBasicVision> = _travelInfo

    private val _driversInfo = MutableLiveData<List<TravelDriverBasicVision>>()
    val driverInfo: LiveData<List<TravelDriverBasicVision>> = _driversInfo

    private val _travelInfractionsInfo = MutableLiveData<List<TravelInfractionInfo>>()
    val travelInfractionsInfo: LiveData<List<TravelInfractionInfo>> = _travelInfractionsInfo

    private val _driversInfractions = MutableLiveData<List<TravelDriverInfractions>>()
    val driversInfractions: LiveData<List<TravelDriverInfractions>> = _driversInfractions

    fun setToken(token: String){
        repository = TravelRepository(token)
    }

    fun concluirAnalise(idViagem: Int) {
        viewModelScope.launch {
            carregando.value = true
            try {
                val request = TravelAnalyzeRequest(wasAnalyzed = true)
                repository?.updateTravelStatus(idViagem, request)
                _viagemAnalisada.value = true
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                carregando.value = false
            }
        }
    }

    fun buscarStatusAnalise(idViagem: Int) {
        viewModelScope.launch {
            try {
                val status = repository?.getTravelAnalysisStatus(idViagem)
                _viagemAnalisada.value = status?.wasAnalyzed ?: false
            } catch (e: Exception) {
                e.printStackTrace()
                _viagemAnalisada.value = false
            }
        }
    }


    fun getTravelsInfo(id: Int) {
        viewModelScope.launch {
            carregando.value = true
            try {
                repository?.let{
                    val response = it.getTravelsInfo(id)
                    _travelInfo.value = response
                } ?: throw IllegalStateException("Token não definido!")
            } catch (e: Exception){
                e.printStackTrace()
            }finally {
                carregando.value = false
            }
        }
    }

    fun getDriversInfo(id: Int) {
        viewModelScope.launch {
            carregando.value = true
            try {
                repository?.let{
                    val response = it.getDriversInfo(id)
                    _driversInfo.value = response
                } ?: throw IllegalStateException("Token não definido!")
            } catch (e: Exception){
                e.printStackTrace()
            }finally {
                carregando.value = false
            }
        }
    }

    fun getDriversInfractions(id: Int) {
        viewModelScope.launch {
            carregando.value = true
            try {
                repository?.let{
                    val response = it.getDriversInfractions(id)
                    _driversInfractions.value = response
                } ?: throw IllegalStateException("Token não definido!")
            } catch (e: Exception){
                e.printStackTrace()
            }finally {
                carregando.value = false
            }
        }
    }

    fun getInfractionsByGravity(id: Int) {
        viewModelScope.launch {
            carregando.value = true
            try {
                repository?.let{
                    val response = it.getInfractionsByGravity(id)
                    _travelInfractionsInfo.value = response
                } ?: throw IllegalStateException("Token não definido!")
            } catch (e: Exception){
                e.printStackTrace()
            }finally {
                carregando.value = false
            }
        }
    }
}