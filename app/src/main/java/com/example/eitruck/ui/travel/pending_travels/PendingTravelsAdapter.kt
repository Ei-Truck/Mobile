package com.example.eitruck.ui.travel.pending_travels

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eitruck.R
import com.example.eitruck.model.Travel
import com.example.eitruck.ui.travel.TravelsViewHolder
import java.util.Date

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
        val formatoEntrada = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val formatoSaida = java.text.SimpleDateFormat("dd/MM/yyyy")


        val travel = travels[position]
        holder.placa.text = travel.placa_caminhao

        val data: Date = formatoEntrada.parse(travel.data_inicio_viagem)
        holder.data.text = formatoSaida.format(data)

        holder.pontos.text = travel.pontuacao_total.toString()

        if (travel.analisada){
            holder.alerta.visibility = View.GONE
        } else {
            holder.alerta.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return travels.size
    }
}