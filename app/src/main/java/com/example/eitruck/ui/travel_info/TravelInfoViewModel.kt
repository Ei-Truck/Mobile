package com.example.eitruck.ui.travel_info

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eitruck.data.remote.repository.postgres.TravelRepository
import com.example.eitruck.model.TravelInfo
import kotlinx.coroutines.launch

class TravelInfoViewModel: ViewModel() {

    private var repository: TravelRepository? = null

    private val _travelInfo = MutableLiveData<List<TravelInfo>>()
    val travelIfo: LiveData<List<TravelInfo>> = _travelInfo

    fun setToken(token: String){
        repository = TravelRepository(token)
    }

    fun getTravelsInfo() {
        viewModelScope.launch {
            try {
                repository?.let{
                    val response = it.getTravelsInfo()
                    _travelInfo.value = response
                } ?: throw IllegalStateException("Token não definido!")
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}