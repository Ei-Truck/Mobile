package com.example.eitruck.ui.travel.analyzed_travels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eitruck.model.Travel
import com.example.eitruck.data.remote.repository.postgres.TravelRepository
import com.example.eitruck.model.NotificationResponse
import com.example.eitruck.utils.NotificationManager
import com.example.eitruck.utils.TravelPreferences
import kotlinx.coroutines.launch

class PendingTravelsViewModel : ViewModel() {

    private var repository: TravelRepository? = null

    var userId = -1

    private val _travels = MutableLiveData<List<Travel>>()
    val travelsLiveData: LiveData<List<Travel>> get() = _travels

    private val carregando = MutableLiveData<Boolean>()
    val carregandoLiveData: LiveData<Boolean> get() = carregando

    fun setToken(token: String) {
        repository = TravelRepository(token)
    }

    fun getTravels(context: Context) {
        viewModelScope.launch {
            carregando.value = true
            try {
                repository?.let {
                    val response = it.getTravels()
                    val pendingTravels = response.filter { travel -> !travel.analisada }

                    val prefs = TravelPreferences(context)
                    val oldIds = prefs.getTravelIds()
                    val newIds = pendingTravels.map { it.idViagem.toString() }.toSet()
                    val newlyAdded = newIds.subtract(oldIds)

                    if (newlyAdded.isNotEmpty()) {
                        val notificationManager = NotificationManager(context)
                        newlyAdded.forEach { id ->
                            val travel = pendingTravels.first { it.idViagem.toString() == id }
                            notificationManager.sendNotification(userId.toString(),
                                NotificationResponse(
                                    id = travel.idViagem.toString(),
                                    title = "Nova Viagem",
                                    message = "Viagem de placa ${travel.placa_caminhao} pronta para analise!",
                                    createdAt = ""
                                )
                            )
                        }
                    }

                    prefs.saveTravelIds(oldIds + newlyAdded)

                    _travels.value = pendingTravels
                } ?: throw IllegalStateException("Token não definido!")
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                carregando.value = false
            }
        }
    }

}

