package com.example.eitruck.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.eitruck.R
import com.example.eitruck.model.NotificationRequest
import com.example.eitruck.model.NotificationResponse
import com.example.eitruck.repository.redis.NotificationsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class   NotificationService(private val context: Context) {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val notificationsRepository: NotificationsRepository = NotificationsRepository()

    private val channelId = "meu_canal"

    init {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Notificações",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showNotification(notificationResponse: NotificationResponse) {
        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle(notificationResponse.title)
            .setContentText(notificationResponse.message)
            .setSmallIcon(R.drawable.notification_icon)
            .build()

        notificationManager.notify(1, notification)
    }

    fun sendNotification(id: String, notificationResponse: NotificationResponse) {
        showNotification(notificationResponse)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val request = NotificationRequest(
                    title = notificationResponse.title,
                    message = notificationResponse.message
                )
                val response = notificationsRepository.createNotification(id, request)

                showNotification(response)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}
