package com.example.eitruck.ui.travel

import TabsAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.eitruck.R
import com.example.eitruck.databinding.FragmentTravelBinding
import com.google.android.material.tabs.TabLayoutMediator

class TravelFragment : Fragment() {

    private lateinit var binding: FragmentTravelBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTravelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tab = binding.tabTravels
        val pagina = binding.pagesTravels

        val adapter = TabsAdapter(requireActivity())
        pagina.adapter = adapter

        TabLayoutMediator(tab , pagina) { tab, position ->
            val tabView = LayoutInflater.from(requireContext()).inflate(R.layout.tab_customizado, null)
            val icon = tabView.findViewById<ImageView>(R.id.tab_icon)
            val text = tabView.findViewById<TextView>(R.id.tab_text)

            when (position) {
                0 -> {
                    icon.setImageResource(R.drawable.icon_analisadas)
                    text.text = "Analisadas"
                }
                1 -> {
                    icon.setImageResource(R.drawable.icon_pendentes)
                    text.text = "Pendentes"
                }
            }
            tab.customView = tabView
        }.attach()


    }

}