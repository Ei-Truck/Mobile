package com.example.eitruck.ui.chat_bot

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eitruck.R
import com.example.eitruck.databinding.ActivityChatBotBinding
import com.example.eitruck.model.Mensagem

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
                messages.add(Mensagem(texto, true))
                adapter.notifyItemInserted(messages.size - 1)

                recyclerView.scrollToPosition(messages.size - 1)

                binding.texto.text.clear()
            }
        }

    }

}