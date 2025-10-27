package com.example.eitruck.ui.chat_bot


import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eitruck.R
import com.example.eitruck.data.local.LoginSave
import com.example.eitruck.data.remote.repository.ChatRepository
import com.example.eitruck.databinding.ActivityChatBotBinding
import com.example.eitruck.model.AskChatBot
import com.example.eitruck.model.Mensagem
import kotlinx.coroutines.launch

class ChatBot : AppCompatActivity() {

    private lateinit var binding: ActivityChatBotBinding
    private lateinit var chatBotRepository: ChatRepository



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chat_bot)

        binding = ActivityChatBotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recyclerView = binding.messagesList

        val messages = mutableListOf<Mensagem>()
        val adapter = ChatBotAdapter(messages)


        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        chatBotRepository = ChatRepository()
        val user = LoginSave(this).getPrefes().getInt("user_id", 0)


        binding.sent.setOnClickListener {
            val texto = binding.texto.text.toString()
            if (texto.isNotEmpty()) {
                messages.add(Mensagem(texto, true, false))
                adapter.notifyItemInserted(messages.size - 1)
                recyclerView.scrollToPosition(messages.size - 1)
                binding.texto.text.clear()

                val loadingMessage = Mensagem("", false, true)
                messages.add(loadingMessage)
                val loadingIndex = messages.size - 1
                adapter.notifyItemInserted(loadingIndex)
                recyclerView.scrollToPosition(loadingIndex)

                lifecycleScope.launch {
                    binding.sent.isEnabled = false
                    try {
                        val response = chatBotRepository.sendAsk(
                            AskChatBot(user, 2, texto)
                        )

                        val reply = response.content.answer ?: "Desculpe, não entendi."

                        messages[loadingIndex] = Mensagem(reply, false, false)
                        adapter.notifyItemChanged(loadingIndex)

                    } catch (e: Exception) {
                        messages[loadingIndex] = Mensagem("Erro: ${e.message}", false, false)
                        adapter.notifyItemChanged(loadingIndex)
                    } finally {
                        binding.sent.isEnabled = true
                    }
                }
            }
        }

        binding.backToHome.setOnClickListener{
            finish()
        }

    }

}