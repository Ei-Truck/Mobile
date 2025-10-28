package com.example.eitruck.ui.travel_info

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eitruck.R

class TravelInfoViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textDescription: TextView = itemView.findViewById(R.id.infra_descrip)
    val textViewInfractionsCount: TextView = itemView.findViewById(R.id.infra_number)
    val Image: ImageView = itemView.findViewById(R.id.infra_lvl)
}