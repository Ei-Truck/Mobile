package com.example.eitruck.ui.home

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eitruck.R

class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val nomeMotorista: TextView = itemView.findViewById(R.id.nome_motorista)
    val pontuacao: TextView = itemView.findViewById(R.id.pontuacao)
    val posicao: TextView = itemView.findViewById(R.id.posicao)

}