package com.example.eitruck.ui.home

import MotoristaRanking
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eitruck.R
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.data.*


class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val combinedChart = view.findViewById<CombinedChart>(R.id.graficoSemanal)

        val valores = arrayOf(7f, 15f, 12f, 11f, 20f)
        val labels = arrayOf("Segunda", "Terça", "Quarta", "Quinta", "Sexta")

        HomeGraph(combinedChart, valores, labels, requireContext())

        val lista: List<MotoristaRanking> = listOf(
            MotoristaRanking(1, "Motorista fuiweiovjewiojewiogj", 1000),
            MotoristaRanking(2, "Motorista 2", 750),
            MotoristaRanking(3, "Motorista 3", 500),
            MotoristaRanking(4, "Motorista 4", 250),
            MotoristaRanking(5, "Motorista 5", 100)
            ,
            MotoristaRanking(6, "Motorista 5", 100),
            MotoristaRanking(7, "Motorista 5", 100),
            MotoristaRanking(8, "Motorista 5", 100),
            MotoristaRanking(9, "Motorista 5", 100),
            MotoristaRanking(10, "Motorista 5", 100)
        )

        val recyclerView = view.findViewById<RecyclerView>(R.id.ranking)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = HomeAdapter(lista)
        recyclerView.isNestedScrollingEnabled = false

        recyclerView.post {
            recyclerView.expand()
        }
    }


    // Esse código é para expandir o recycler view
    fun RecyclerView.expand() {
        val adapter = adapter ?: return
        var totalHeight = 0
        for (i in 0 until adapter.itemCount) {
            val holder = adapter.createViewHolder(this, adapter.getItemViewType(i))
            adapter.onBindViewHolder(holder, i)
            holder.itemView.measure(
                View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.UNSPECIFIED
            )
            totalHeight += holder.itemView.measuredHeight
        }
        layoutParams.height = totalHeight
        requestLayout()
    }

}
