package com.example.eitruck.ui.travel.analyzed_travels

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eitruck.R
import com.example.eitruck.model.Travel

class AnalyzedTravelsAdapter(
    private val travels: List<Travel>
): RecyclerView.Adapter<AnalyzedTravelsViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AnalyzedTravelsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_travel, parent, false)
        return AnalyzedTravelsViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: AnalyzedTravelsViewHolder,
        position: Int
    ) {
        val travel = travels[position]
        holder.placa.text = travel.placa
        holder.data.text = travel.data
        holder.pontos.text = travel.pontos.toString()

    }

    override fun getItemCount(): Int {
        return travels.size
    }
}