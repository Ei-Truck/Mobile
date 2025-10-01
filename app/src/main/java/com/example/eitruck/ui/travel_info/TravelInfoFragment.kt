package com.example.eitruck.ui.travel_info

import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.VideoView
import androidx.core.content.ContextCompat
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eitruck.R
import com.example.eitruck.databinding.FragmentTravelInfoBinding
import com.example.eitruck.model.Driver
import com.example.eitruck.model.Infractions
import com.example.eitruck.model.Midia
import com.example.eitruck.model.TravelInfo

class TravelInfoFragment : Fragment() {

    private lateinit var binding: FragmentTravelInfoBinding
    private var motoristaAtual: Int = 0
    private var player: ExoPlayer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTravelInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val infracoes = listOf(
            Infractions(1,1,"Leve"),
            Infractions(2,2,"Média"),
            Infractions(3,3,"Grave"),
            Infractions(4,4,"Muito Grave")
        )

        val driver = listOf(
            Driver(1, "Paulo Munhoz", "Moderado", "15:32h", "17:50h"),
            Driver(2, "Matheus Bastos", "Leve", "18:00h", "12:00h")
        )

        val midia = listOf(
            Midia(1, "https://eitruck.s3.sa-east-1.amazonaws.com/infracoes/cara_dirigindo.mp4"),
            Midia(2, "https://eitruck.s3.sa-east-1.amazonaws.com/infracoes/mexirica.mp4")
        )

        val travel = TravelInfo(1, "ABC1D34","14/08","15/08", 100, "Bovinos", driver, infracoes, midia)

        val maximo = travel.motorista.size

        binding.carPlate.text = travel.placa
        binding.startDt.text = travel.starDate
        binding.endDt.text = travel.endDate
        binding.km.text = "${travel.km} Km"
        binding.segment.text = travel.segment
        binding.totalInfractions.text = travel.infracoes.size.toString()

        val adapter = TravelInfoAdapter(infracoes)
        binding.infracoes.layoutManager = LinearLayoutManager(requireContext())
        binding.infracoes.adapter = adapter

        binding.btnPreviousVideo.setOnClickListener {
            if (motoristaAtual > 0) {
                motoristaAtual--
                loadInformacoes(travel)
                loadMidia(travel)
                mudarBotao(maximo)
            }
        }

        binding.btnNextVideo.setOnClickListener {
            if (motoristaAtual < maximo - 1) {
                motoristaAtual++
                loadInformacoes(travel)
                loadMidia(travel)
                mudarBotao(maximo)
            }
        }

        loadInformacoes(travel)
        loadMidia(travel)

        binding.infracoes.isNestedScrollingEnabled = false

        mudarBotao(maximo)
    }

    private fun loadInformacoes(travel: TravelInfo){
        binding.driverName.text = travel.motorista[motoristaAtual].nome
        binding.driverHour.text = "${travel.motorista[motoristaAtual].horarioComeco} - ${travel.motorista[motoristaAtual].horarioFim}"
        binding.driverRisk.text = "Risco: ${travel.motorista[motoristaAtual].risco}"
    }

    private fun loadMidia(travel: TravelInfo){
        if (player == null) {
            player = ExoPlayer.Builder(requireContext()).build()
            binding.playerView.player = player
        }

        val mediaItem = MediaItem.fromUri(travel.midia[motoristaAtual].midia)
        player?.apply {
            setMediaItem(mediaItem)
            prepare()
            play()
        }
    }

    override fun onStop() {
        super.onStop()
        player?.pause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        player?.release()
        player = null
    }

    fun mudarBotao(maximo: Int) {
        if (motoristaAtual == maximo - 1) {
            binding.btnNextVideo.isEnabled = false
            binding.btnNextVideo.imageTintList = ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.colorBackground)
            )
        } else {
            binding.btnNextVideo.isEnabled = true
            binding.btnNextVideo.imageTintList = ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark)
            )
        }

        if (motoristaAtual == 0) {
            binding.btnPreviousVideo.isEnabled = false
            binding.btnPreviousVideo.imageTintList = ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.colorBackground)
            )
        } else {
            binding.btnPreviousVideo.isEnabled = true
            binding.btnPreviousVideo.imageTintList = ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark)
            )
        }
    }



}
