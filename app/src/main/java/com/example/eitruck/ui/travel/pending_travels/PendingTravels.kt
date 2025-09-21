package com.example.eitruck.ui.travel.pending_travels

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eitruck.R
import com.example.eitruck.databinding.FragmentPendingTravelsBinding
import com.example.eitruck.model.Travel
import com.example.eitruck.model.Truck
import com.example.eitruck.ui.travel.analyzed_travels.PendingTravelsViewModel
import java.sql.Date

class PendingTravels : Fragment() {

    private lateinit var binding: FragmentPendingTravelsBinding
    private val viewModel: PendingTravelsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPendingTravelsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.carregandoLiveData.observe(viewLifecycleOwner) { carregando ->
            if (carregando) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        viewModel.travelsLiveData.observe(viewLifecycleOwner) { travels ->
            val adapter = PendingTravelsAdapter(travels)
            binding.pendingRecycler.layoutManager = LinearLayoutManager(requireContext())
            binding.pendingRecycler.adapter = adapter
        }

        if (viewModel.travelsLiveData.value.isNullOrEmpty()) {
            viewModel.getTravels()
        }

    }

}