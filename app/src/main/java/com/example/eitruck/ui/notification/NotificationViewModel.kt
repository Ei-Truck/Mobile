package com.example.eitruck.ui.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eitruck.model.NotificationResponse
import com.example.eitruck.repository.redis.NotificationsRepository

class NotificationViewModel: ViewModel() {

    private val repository = NotificationsRepository()

    private val _notifications = MutableLiveData<List<NotificationResponse>>()
    val notifications: LiveData<List<NotificationResponse>> = _notifications

    private val carregando = MutableLiveData<Boolean>()
    val carregandoLiveData: LiveData<Boolean> get() = carregando

    suspend fun getNotifications(id: String) {
        carregando.value = true
        try {
            val response = repository.getNotifications(id)
            _notifications.value = response
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            carregando.value = false
        }
    }

}