package com.example.eitruck.ui.travel.analyzed_travels

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eitruck.R
import com.example.eitruck.model.Travel
import com.example.eitruck.ui.travel.TravelsViewHolder
import java.util.Date

class AnalyzedTravelsAdapter(
    private var travels: List<Travel>
): RecyclerView.Adapter<TravelsViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TravelsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_travel, parent, false)
        return TravelsViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: TravelsViewHolder,
        position: Int
    ) {
        val formatoEntrada = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val formatoSaida = java.text.SimpleDateFormat("dd/MM/yyyy")


        val travel = travels[position]
        holder.placa.text = travel.caminhao.placa

        val data: Date = formatoEntrada.parse(travel.dtHrInicio)
        holder.data.text = formatoSaida.format(data)

        holder.pontos.text = travel.pontuacao.toString()

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