package com.example.eitruck.ui.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eitruck.R
import com.example.eitruck.model.NotificationResponse
import java.text.SimpleDateFormat

class NotificationAdapter(
    val notificationResponses: List<NotificationResponse>
): RecyclerView.Adapter<NotificationViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_notification, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: NotificationViewHolder,
        position: Int
    ) {
        val notification = notificationResponses[position]
        holder.title.text = notification.title
        holder.message.text = notification.message

        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val outputFormat = SimpleDateFormat("dd/MM/yyyy - HH:mm")
        val date = inputFormat.parse(notification.createdAt)
        holder.date.text = outputFormat.format(date)

    }

    override fun getItemCount(): Int {
        return notificationResponses.size
    }
}