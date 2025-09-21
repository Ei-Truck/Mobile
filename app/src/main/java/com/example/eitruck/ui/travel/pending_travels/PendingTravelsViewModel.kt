package com.example.eitruck.ui.travel.analyzed_travels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eitruck.model.Travel
import com.example.eitruck.repository.TravelRepository
import kotlinx.coroutines.launch

class PendingTravelsViewModel : ViewModel() {

    private val repository = TravelRepository()

    private val _travels = MutableLiveData<List<Travel>>()

    val travelsLiveData: LiveData<List<Travel>> get() = _travels

    private val carregando = MutableLiveData<Boolean>()
    val carregandoLiveData: LiveData<Boolean> get() = carregando

    fun getTravels() {
        viewModelScope.launch {
            carregando.value = true
            try {
                val response = repository.getTravels()
                _travels.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                carregando.value = false
            }
        }
    }
}
