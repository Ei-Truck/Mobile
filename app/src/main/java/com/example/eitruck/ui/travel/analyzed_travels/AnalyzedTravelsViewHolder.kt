package com.example.eitruck.ui.travel.analyzed_travels

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eitruck.R

class AnalyzedTravelsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    val placa: TextView = itemView.findViewById(R.id.car_plate)
    val data: TextView = itemView.findViewById(R.id.car_date)
    val pontos: TextView = itemView.findViewById(R.id.textView32)
    val alerta: ImageView = itemView.findViewById(R.id.imageView7)
}