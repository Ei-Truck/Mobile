package com.example.eitruck.ui.chat_bot

import android.R.attr.content
import android.R.id.content
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eitruck.R
import com.example.eitruck.api.GeminiService
import com.example.eitruck.databinding.ActivityChatBotBinding
import com.example.eitruck.model.Mensagem
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatBot : AppCompatActivity() {

    private lateinit var binding: ActivityChatBotBinding

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
                    try {
                        val response = withContext(Dispatchers.IO) {
                            GeminiService.model.generateContent(
                                content { text(texto) }
                            )
                        }

                        val reply = response.text ?: "..."
                        messages[loadingIndex] = Mensagem(reply, false, false)
                        adapter.notifyItemChanged(loadingIndex)

                    } catch (e: Exception) {
                        messages[loadingIndex] = Mensagem("Erro: ${e.message}", false, false)
                        adapter.notifyItemChanged(loadingIndex)
                    }
                }
            }
        }


    }

}