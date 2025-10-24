package com.example.eitruck.ui.travel_info

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eitruck.R
import com.example.eitruck.model.InfractionsTravelInfo

class TravelInfoAdapter(
    private var infracoes: List<InfractionsTravelInfo>

): RecyclerView.Adapter<TravelInfoViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TravelInfoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_infracao, parent, false)
        return TravelInfoViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: TravelInfoViewHolder,
        position: Int
    ) {

        val infracao = infracoes[position]
        holder.textDescription.text = infracao.tipoInfracao
        holder.textViewInfractionsCount.text = infracao.quantidade.toString()

        when (infracao.tipoInfracao) {
            "Leve" -> {
                holder.Image.setImageResource(R.drawable.ic_eclipse_leve)
            }
            "Média" -> {
                holder.Image.setImageResource(R.drawable.ic_eclipse_media)
            }
            "Grave" -> {
                holder.Image.setImageResource(R.drawable.ic_eclipse_grave)
            }
            "Muito Grave" -> {
                holder.Image.setImageResource(R.drawable.ic_eclipse_gravissima)
            }
            else -> {
                holder.Image.setImageResource(R.drawable.ic_eclipse_gravissima)
            }
        }

    }

    override fun getItemCount(): Int {
        return infracoes.size
    }

}