package com.example.eitruck.ui.chat_bot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eitruck.R
import com.example.eitruck.model.Mensagem

class ChatBotAdapter(
    val menssagens: List<Mensagem>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>()  {

    private val TYPE_SENT = 1
    private val TYPE_RECEIVED = 2

    override fun getItemViewType(position: Int): Int {
        return if (menssagens[position].isSent) {
            TYPE_SENT
        } else {
            TYPE_RECEIVED
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_SENT) {
            val view = inflater.inflate(R.layout.card_message_user, parent, false)
            SentViewHolder(view)
        } else {
            val view = inflater.inflate(R.layout.card_message_chat, parent, false)
            ReceivedViewHolder(view)
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val mensagem = menssagens[position]
        if (mensagem.isSent) {
            (holder as SentViewHolder).bind(mensagem)
        } else {
            (holder as ReceivedViewHolder).bind(mensagem)

        }
    }

    override fun getItemCount(): Int {
        return menssagens.size
    }

    class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(msg: Mensagem) {
            itemView.findViewById<TextView>(R.id.txt_chat).text = msg.texto
        }
    }

    class ReceivedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(msg: Mensagem) {
            val txtChat = itemView.findViewById<TextView>(R.id.txt_chat)
            val loadingView = itemView.findViewById<ImageView>(R.id.loading)

            if (msg.isLoading) {
                txtChat.visibility = View.GONE
                Glide.with(itemView.context)
                    .load(R.drawable.loading)
                    .into(loadingView)
                loadingView.visibility = View.VISIBLE
            } else {
                txtChat.visibility = View.VISIBLE
                loadingView.visibility = View.GONE
                txtChat.text = msg.texto
            }
        }
    }


}