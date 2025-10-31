package com.example.eitruck.ui.travel.analyzed_travels

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eitruck.databinding.FragmentAnalyzedTravelsBinding
import com.example.eitruck.ui.login.Login
import com.example.eitruck.data.local.LoginSave

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

        val token = LoginSave(requireContext(), null).getToken()
        if (!token.isNullOrEmpty()) {
            viewModel.setToken(token)
        } else {
            val intent = Intent(requireContext(), Login::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

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

    override fun onResume() {
        super.onResume()
        viewModel.getTravels()
    }
}