package com.example.eitruck.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eitruck.data.remote.repository.postgres.InfractionsRepository
import com.example.eitruck.model.WeeklyReport
import kotlinx.coroutines.launch


class HomeViewModel: ViewModel() {

    private var repository: InfractionsRepository? = null

    private val _infractions = MutableLiveData<List<WeeklyReport>>()
    val infractions: MutableLiveData<List<WeeklyReport>> get() = _infractions

    private val carregando = MutableLiveData<Boolean>()
    val carregandoLiveData: MutableLiveData<Boolean> get() = carregando

    fun setToken(token: String){
        repository = InfractionsRepository(token)
    }

    fun getWeeklyReport(){
        carregando.value = true
        viewModelScope.launch {
            try {
                repository?.let {
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
}