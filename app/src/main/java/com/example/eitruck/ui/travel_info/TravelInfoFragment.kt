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


        val player = ExoPlayer.Builder(requireContext()).build()
        val playerView = binding.playerView
        playerView.player = player

        val mediaItem = MediaItem.fromUri("https://res.cloudinary.com/dujtlyl7w/video/upload/v1758659007/WIN_20240304_11_21_37_Pro_nbiiby.mp4")
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()



    }

}