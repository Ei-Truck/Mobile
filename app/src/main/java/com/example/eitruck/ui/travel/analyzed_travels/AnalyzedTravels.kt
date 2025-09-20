package com.example.eitruck.ui.travel.analyzed_travels

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eitruck.databinding.FragmentAnalyzedTravelsBinding
import com.example.eitruck.model.Travel

class AnalyzedTravels : Fragment() {

    private lateinit var binding: FragmentAnalyzedTravelsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAnalyzedTravelsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viagens = listOf(
            Travel("ABC1D23", "19/09/2025", 600, true),
            Travel("ABC1D23", "19/09/2025", 600, true),
            Travel("ABC1D23", "19/09/2025", 600, true),
            Travel("ABC1D23", "19/09/2025", 600, true),
            Travel("ABC1D23", "19/09/2025", 600, true),
        )

        val adapter = AnalyzedTravelsAdapter(viagens)
        binding.analyzedRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.analyzedRecycler.adapter = adapter
    }
}