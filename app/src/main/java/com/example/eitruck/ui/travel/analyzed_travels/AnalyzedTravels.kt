package com.example.eitruck.ui.travel.analyzed_travels

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eitruck.databinding.FragmentAnalyzedTravelsBinding
import com.example.eitruck.model.Travel
import com.example.eitruck.model.Truck
import java.sql.Date

class AnalyzedTravels : Fragment() {

    private lateinit var binding: FragmentAnalyzedTravelsBinding
    private val viewModel: AnalyzedTravelsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAnalyzedTravelsBinding.inflate(inflater, container, false)
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
            val adapter = AnalyzedTravelsAdapter(travels)
            binding.analyzedRecycler.layoutManager = LinearLayoutManager(requireContext())
            binding.analyzedRecycler.adapter = adapter
        }

        if (viewModel.travelsLiveData.value.isNullOrEmpty()) {
            viewModel.getTravels()
        }
    }

}