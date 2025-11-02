package com.example.eitruck.ui.travel_info

import android.app.Dialog
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.eitruck.data.local.LoginSave
import com.example.eitruck.databinding.FragmentTravelInfoBinding
import com.example.eitruck.model.TravelDriverBasicVision
import com.example.eitruck.model.TravelDriverInfractions
import com.example.eitruck.utils.PdfReportGenerator
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.eitruck.R
import com.example.eitruck.model.TravelInfractionInfo
import com.example.eitruck.ui.tratativa.TratativaNote
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import com.google.firebase.firestore.FirebaseFirestore


class TravelInfoFragment : Fragment() {

    private lateinit var binding: FragmentTravelInfoBinding
    private val viewModel: TravelInfoViewModel by viewModels()
    private val db = FirebaseFirestore.getInstance()
    private var player: ExoPlayer? = null

    private var motoristasFiltrados: List<TravelDriverBasicVision> = emptyList()
    private var driversInfractionsFiltradas: List<TravelDriverInfractions> = emptyList()
    private var typeInfractionsFiltradas: List<TravelInfractionInfo> = emptyList()
    private var currentDriverIndex = 0

    private val tratativasMap = mutableMapOf<Int, Pair<String, String>>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTravelInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val idViagem = arguments?.getInt("id") ?: 0
        viewModel.setToken(LoginSave(requireContext()).getToken().toString())
        viewModel.buscarStatusAnalise(idViagem)

        viewModel.carregandoLiveData.observe(viewLifecycleOwner) {
            binding.progressBar2.visibility = if (it) View.VISIBLE else View.GONE
        }

        viewModel.travelInfo.observe(viewLifecycleOwner) { viagem ->
            if (viagem?.idViagem == idViagem) {
                binding.carPlate.text = viagem.placaCaminhao
                binding.km.text = "${viagem.kmViagem} km(s)"
                binding.segment.text = viagem.segmento

                val formatter = DateTimeFormatter.ofPattern("dd/MM")
                binding.startDt.text = runCatching {
                    OffsetDateTime.parse(viagem.dataInicioViagem, DateTimeFormatter.ISO_OFFSET_DATE_TIME).format(formatter)
                }.getOrDefault("--/--")
                binding.endDt.text = runCatching {
                    OffsetDateTime.parse(viagem.dataFimViagem, DateTimeFormatter.ISO_OFFSET_DATE_TIME).format(formatter)
                }.getOrDefault("--/--")

            }
        }

        viewModel.viagemAnalisada.observe(viewLifecycleOwner) { analisada ->
            if (analisada) {
                binding.btnTratativa.visibility = View.GONE

                binding.btnConcluirAnalise.text = "Análise concluída"
                binding.btnConcluirAnalise.isEnabled = false
                binding.btnConcluirAnalise.setBackgroundColor(
                    ContextCompat.getColor(requireContext(), R.color.colorTertiary)
                )
                binding.btnConcluirAnalise.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))

                binding.btnGerarRelatorio.text = "Ver relatório"
                binding.btnGerarRelatorio.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_icon_eye)
                binding.btnGerarRelatorio.isEnabled = true

                db.collection("tratativas")
                    .whereEqualTo("idViagem", idViagem)
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            val idMotorista = document.getLong("idMotorista")?.toInt() ?: continue
                            val tratativa = document.getString("tratativa") ?: ""
                            val partes = tratativa.split("//")
                            tratativasMap[idMotorista] = Pair(
                                partes.getOrNull(0) ?: "",
                                partes.getOrNull(1) ?: ""
                            )
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), "Erro ao carregar tratativas", Toast.LENGTH_SHORT).show()
                    }
            } else {
                binding.btnTratativa.visibility = View.VISIBLE
                binding.btnTratativa.text = "Adicionar tratativa"
                binding.btnTratativa.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_icon_tratativa)
                binding.btnTratativa.isEnabled = true

                binding.btnConcluirAnalise.text = "Concluir análise"
                binding.btnConcluirAnalise.isEnabled = true

                binding.btnGerarRelatorio.text = "Gerar relatório"
                binding.btnGerarRelatorio.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_icon_relatorio)
                binding.btnGerarRelatorio.isEnabled = true
            }
        }

        viewModel.driverInfo.observe(viewLifecycleOwner) {
            motoristasFiltrados = it.filter { m -> m.idViagem == idViagem }
            verificarEAtualizarMotorista(idViagem)
        }

        viewModel.driversInfractions.observe(viewLifecycleOwner) {
            driversInfractionsFiltradas = it.filter { i -> i.idViagem == idViagem }
            verificarEAtualizarMotorista(idViagem)
        }

        viewModel.travelInfractionsInfo.observe(viewLifecycleOwner) {
            typeInfractionsFiltradas = it.filter { i -> i.idViagem == idViagem }
            verificarEAtualizarMotorista(idViagem)
        }

        viewModel.updatetravel.observe(viewLifecycleOwner) { infractions ->
            if (infractions != null) {
                Toast.makeText(requireContext(), "Análise concluída!", Toast.LENGTH_SHORT).show()
                binding.btnConcluirAnalise.text = "Análise concluída"
                binding.btnConcluirAnalise.isEnabled = false
                binding.btnConcluirAnalise.setBackgroundColor(
                    ContextCompat.getColor(requireContext(), R.color.colorTertiary)
                )
                binding.btnConcluirAnalise.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))

                binding.btnTratativa.isEnabled = true
                binding.btnGerarRelatorio.isEnabled = true
            }
        }

        binding.btnConcluirAnalise.setOnClickListener{
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.modal_analyze)
            dialog.window?.apply {
                setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                setBackgroundDrawableResource(android.R.color.transparent)
                setGravity(Gravity.CENTER)
            }
            dialog.setCancelable(true)
            dialog.show()

            dialog.findViewById<Button>(R.id.btn_analyze).setOnClickListener {
                viewModel.concluirAnalise(idViagem)
                dialog.dismiss()
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }

            dialog.findViewById<Button>(R.id.btn_cancel).setOnClickListener {
                dialog.dismiss()
            }
        }

        binding.btnPreviousVideo.setOnClickListener {
            if (currentDriverIndex > 0) currentDriverIndex-- else Toast.makeText(requireContext(), "Primeiro motorista", Toast.LENGTH_SHORT).show()
            verificarEAtualizarMotorista(idViagem)
        }

        binding.btnNextVideo.setOnClickListener {
            if (currentDriverIndex < motoristasFiltrados.size - 1) currentDriverIndex++ else Toast.makeText(requireContext(), "Último motorista", Toast.LENGTH_SHORT).show()
            verificarEAtualizarMotorista(idViagem)
        }

        binding.btnTratativa.setOnClickListener {
            val motoristaAtual = motoristasFiltrados.getOrNull(currentDriverIndex)
            motoristaAtual?.let {
                val tratativa = tratativasMap[it.idMotorista]
                startActivityForResult(
                    Intent(requireContext(), TratativaNote::class.java).apply {
                        putExtra("textoExistente", tratativa?.second ?: "")
                        putExtra("tituloExistente", tratativa?.first ?: "")
                    }, 1001
                )
            }
        }

        binding.btnGerarRelatorio.setOnClickListener {
            val motorista = motoristasFiltrados.getOrNull(currentDriverIndex) ?: return@setOnClickListener
            val infracao = driversInfractionsFiltradas.firstOrNull { it.idMotorista == motorista.idMotorista }
            val typeInfracao = typeInfractionsFiltradas.firstOrNull { it.idMotorista == motorista.idMotorista }
            val viagem = viewModel.travelInfo.value

            val tratativa = tratativasMap[motorista.idMotorista]
            val tituloTratativa = tratativa?.first ?: ""
            val textoTratativa = tratativa?.second ?: "Nenhuma tratativa adicionada"

            PdfReportGenerator.gerarRelatorioTratativa(
                context = requireContext(),
                titulo = tituloTratativa,
                corpo = textoTratativa,
                motorista = motorista.nomeMotorista,
                placa = viagem?.placaCaminhao ?: "",
                dataInicio = viagem?.dataInicioViagem?.let {
                    runCatching {
                        OffsetDateTime.parse(it, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                            .format(DateTimeFormatter.ofPattern("dd/MM"))
                    }.getOrDefault("--/--")
                } ?: "--/--",
                dataFim = viagem?.dataFimViagem?.let {
                    runCatching {
                        OffsetDateTime.parse(it, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                            .format(DateTimeFormatter.ofPattern("dd/MM"))
                    }.getOrDefault("--/--")
                } ?: "--/--",
                totalInfracoes = infracao?.quantidadeInfracoes?.toString() ?: "0",
                leves = typeInfracao?.tipoLeve?.toString() ?: "0",
                medias = typeInfracao?.tipoMedia?.toString() ?: "0",
                graves = typeInfracao?.tipoGrave?.toString() ?: "0",
                gravissimas = typeInfracao?.tipoGravissima?.toString() ?: "0"
            )

            binding.btnGerarRelatorio.text = "Ver relatório"
            binding.btnGerarRelatorio.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_icon_eye)
        }

        viewModel.getDriversInfo(idViagem)
        viewModel.getDriversInfractions(idViagem)
        viewModel.getTravelsInfo(idViagem)
        viewModel.getInfractionsByGravity(idViagem)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1001 && resultCode == android.app.Activity.RESULT_OK && data != null) {
            val titulo = data.getStringExtra("tratativaTitulo") ?: ""
            val texto = data.getStringExtra("tratativaTexto") ?: ""
            val motoristaAtual = motoristasFiltrados.getOrNull(currentDriverIndex)

            motoristaAtual?.let {
                val tratativaConcatenada = "$titulo//$texto"

                val tratativaData = hashMapOf(
                    "idViagem" to it.idViagem,
                    "idMotorista" to it.idMotorista,
                    "tratativa" to tratativaConcatenada
                )

                db.collection("tratativas")
                    .document("${it.idViagem}_${it.idMotorista}")
                    .set(tratativaData)
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "Tratativa salva com sucesso!", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(requireContext(), "Erro ao salvar tratativa: ${e.message}", Toast.LENGTH_SHORT).show()
                    }

                tratativasMap[it.idMotorista] = Pair(titulo, texto)
            }

            binding.btnTratativa.text = "Ver tratativa"
            binding.btnTratativa.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_icon_lupa)
        }
    }


    private fun atualizarMotorista(index: Int, idViagem: Int) {
        val motorista = motoristasFiltrados.getOrNull(index) ?: return
        val infracao = driversInfractionsFiltradas.firstOrNull { it.idMotorista == motorista.idMotorista }
        val typeInfracao = typeInfractionsFiltradas.firstOrNull { it.idMotorista == motorista.idMotorista }

        binding.driverName.text = motorista.nomeMotorista
        binding.driverRisk.text = "Risco: ${motorista.riscoMotorista}"
        binding.totalInfractions.text = infracao?.quantidadeInfracoes?.toString() ?: "0"
        binding.numTotalLeve.text = typeInfracao?.tipoLeve.toString()
        binding.numTotalMedia.text = typeInfracao?.tipoMedia.toString()
        binding.numTotalGraves.text = typeInfracao?.tipoGrave.toString()
        binding.numTotalGravissima.text = typeInfracao?.tipoGravissima.toString()

        motorista.urlFotoMotorista
            ?.takeIf { it.isNotEmpty() && it != "Sem foto" }
            ?.let { url ->
                Glide.with(requireContext())
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.profileImage)
            } ?: run {
            binding.profileImage.setImageResource(R.drawable.ic_icon_profile)
            binding.profileImage.setColorFilter(
                ContextCompat.getColor(requireContext(), android.R.color.white),
                PorterDuff.Mode.SRC_IN
            )
        }

        motorista.urlMidiaConcatenada?.takeIf { it.isNotEmpty() }?.let { url ->
            if (player == null) {
                player = ExoPlayer.Builder(requireContext()).build()
                binding.playerView.player = player
            }
            player?.setMediaItem(MediaItem.fromUri(url))
            player?.prepare()
            player?.play()
        } ?: player?.pause()

        atualizarBotaoTratativa(motorista.idMotorista)
        atualizarBotaoRelatorio(motorista.idMotorista)
    }

    private fun verificarEAtualizarMotorista(idViagem: Int) {
        if (motoristasFiltrados.isNotEmpty() && currentDriverIndex in motoristasFiltrados.indices) {
            atualizarMotorista(currentDriverIndex, idViagem)
        }
    }

    private fun atualizarBotaoTratativa(motoristaId: Int) {
        val tratativa = tratativasMap[motoristaId]
        if (tratativa != null) {
            binding.btnTratativa.text = "Ver tratativa"
            binding.btnTratativa.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_icon_lupa)
        } else {
            binding.btnTratativa.text = "Adicionar tratativa"
            binding.btnTratativa.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_icon_tratativa)
        }
    }

    private fun atualizarBotaoRelatorio(motoristaId: Int) {
        val tratativa = tratativasMap[motoristaId]
        if (tratativa != null) {
            binding.btnGerarRelatorio.text = "Ver relatório"
            binding.btnGerarRelatorio.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_icon_eye)
        } else {
            binding.btnGerarRelatorio.text = "Gerar relatório"
            binding.btnGerarRelatorio.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_icon_relatorio)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        player?.release()
        player = null
    }
}