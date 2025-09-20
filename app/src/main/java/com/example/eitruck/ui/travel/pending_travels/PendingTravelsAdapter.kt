package com.example.eitruck.ui.travel.pending_travels

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eitruck.R
import com.example.eitruck.model.Travel
import com.example.eitruck.ui.travel.TravelsViewHolder

class PendingTravelsAdapter(
    val travels: List<Travel>
): RecyclerView.Adapter<TravelsViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TravelsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_travel, parent, false)
        return TravelsViewHolder(view)
    }


    override fun onBindViewHolder(holder: TravelsViewHolder, position: Int) {
        val travel = travels[position]
        holder.placa.text = travel.placa
        holder.data.text = travel.data
        holder.pontos.text = travel.pontos.toString()

        if (travel.tratada){
            holder.alerta.visibility = View.GONE
        } else {
            holder.alerta.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return travels.size
    }
}