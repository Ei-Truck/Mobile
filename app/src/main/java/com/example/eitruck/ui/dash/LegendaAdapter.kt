package com.example.eitruck.ui.dash

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eitruck.R
import com.example.eitruck.model.DashLegendaItem

class LegendaAdapter(private var lista: List<DashLegendaItem>) :
    RecyclerView.Adapter<LegendaAdapter.LegendaViewHolder>() {

    class LegendaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val corView: View = view.findViewById(R.id.viewCor)
        val txtNome: TextView = view.findViewById(R.id.txtNome)
        val txtQtd: TextView = view.findViewById(R.id.txtQtd)
        val txtPerc: TextView = view.findViewById(R.id.txtPerc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LegendaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_legenda, parent, false)
        return LegendaViewHolder(view)
    }

    override fun onBindViewHolder(holder: LegendaViewHolder, position: Int) {
        val item = lista[position]
        holder.corView.setBackgroundColor(item.cor)
        holder.txtNome.text = item.tipo_infracao
        holder.txtQtd.text = item.total_ocorrencias.toString()
        holder.txtPerc.text = item.porcentagem_do_total
    }

    override fun getItemCount() = lista.size

    fun updateData(novaLista: List<DashLegendaItem>) {
        lista = novaLista
        notifyDataSetChanged()
    }
}
