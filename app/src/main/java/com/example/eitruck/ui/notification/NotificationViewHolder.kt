package com.example.eitruck.ui.notification

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eitruck.R

class NotificationViewHolder(itemV: View): RecyclerView.ViewHolder(itemV) {
    val title: TextView = itemV.findViewById(R.id.title_notification)
    val message: TextView = itemV.findViewById(R.id.message_notification)
    val date: TextView = itemV.findViewById(R.id.date_notification)
}
