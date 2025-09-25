package com.example.eitruck.ui.travel_info

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.VideoView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eitruck.R
import com.example.eitruck.databinding.FragmentTravelInfoBinding
import com.example.eitruck.model.Infractions

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


        val player = ExoPlayer.Builder(requireContext()).build()
        val playerView = binding.playerView
        playerView.player = player

        val mediaItem = MediaItem.fromUri("https://eitruck.s3.sa-east-1.amazonaws.com/infracoes/cara_dirigindo.mp4")
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()

        val infracoes = listOf(
            Infractions(1,1,"Leve"),
            Infractions(2,2,"Média"),
            Infractions(3,3,"Grave"),
            Infractions(4,4,"Muito Grave")
        )

        val adapter = TravelInfoAdapter(infracoes)
        binding.infracoes.layoutManager = LinearLayoutManager(requireContext())
        binding.infracoes.adapter = adapter

        binding.infracoes.isNestedScrollingEnabled = false


    }

}