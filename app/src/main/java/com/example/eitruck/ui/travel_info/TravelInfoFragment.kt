package com.example.eitruck.ui.travel_info

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.eitruck.R
import com.example.eitruck.databinding.FragmentTravelInfoBinding

class TravelInfoFragment : Fragment() {

    private lateinit var binding: FragmentTravelInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTravelInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getInt("id")
        binding.textId.text = "Carregado com ID: $id"

    }

}