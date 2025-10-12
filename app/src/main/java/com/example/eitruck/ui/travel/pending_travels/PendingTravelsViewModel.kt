package com.example.eitruck.ui.travel.analyzed_travels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eitruck.model.Travel
import com.example.eitruck.data.remote.repository.postgres.TravelRepository
import kotlinx.coroutines.launch

class PendingTravelsViewModel : ViewModel() {

    private var repository: TravelRepository? = null

    private val _travels = MutableLiveData<List<Travel>>()
    val travelsLiveData: LiveData<List<Travel>> get() = _travels

    private val carregando = MutableLiveData<Boolean>()
    val carregandoLiveData: LiveData<Boolean> get() = carregando

    fun setToken(token: String) {
        repository = TravelRepository(token)
    }

    fun getTravels() {
        viewModelScope.launch {
            carregando.value = true
            try {
                repository?.let {
                    val response = it.getTravels()
                    _travels.value = response.filter { travel ->
                        !travel.tratada
                    }
                } ?: throw IllegalStateException("Token não definido!")
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                carregando.value = false
            }
        }
    }
}

