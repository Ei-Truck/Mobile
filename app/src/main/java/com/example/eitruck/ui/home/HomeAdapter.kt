package com.example.eitruck.ui.home

import MotoristaRanking
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.eitruck.R

class HomeAdapter (
    private var motoristas: List<MotoristaRanking>
) : RecyclerView.Adapter<HomeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_ranking, parent, false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val motorista = motoristas[position]
        holder.nomeMotorista.text = motorista.nome
        holder.pontuacao.text = motorista.pontuacao.toString()
        holder.posicao.text = motorista.posicao.toString()

        val drawable = holder.posicao.background.mutate()
        if (drawable is GradientDrawable) {
            val cor = ContextCompat.getColor(holder.itemView.context, motorista.cor)
            drawable.setColor(cor)
        }
    }



    override fun getItemCount(): Int {
        return motoristas.size
    }

    fun updateData(novaLista: List<MotoristaRanking>) {
        motoristas = novaLista
        notifyDataSetChanged()
    }



}