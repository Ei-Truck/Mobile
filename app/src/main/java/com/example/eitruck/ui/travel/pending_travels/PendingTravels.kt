package com.example.eitruck.ui.travel.pending_travels

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eitruck.R
import com.example.eitruck.databinding.FragmentPendingTravelsBinding
import com.example.eitruck.model.Travel

class PendingTravels : Fragment() {

    private lateinit var binding: FragmentPendingTravelsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPendingTravelsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viagens = listOf(
            Travel("ABC1D23", "19/09/2025", 600, false),
            Travel("ABC1D23", "19/09/2025", 600, false),
            Travel("ABC1D23", "19/09/2025", 600, false),
            Travel("ABC1D23", "19/09/2025", 600, false),
            Travel("ABC1D23", "19/09/2025", 600, false),
        )

        val adapter = PendingTravelsAdapter(viagens)
        binding.pendingRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.pendingRecycler.adapter = adapter


    }

}