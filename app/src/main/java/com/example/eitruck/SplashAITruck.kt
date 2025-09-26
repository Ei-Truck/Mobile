package com.example.eitruck

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.eitruck.databinding.ActivitySplashAitruckBinding

class SplashAITruck : AppCompatActivity() {

    private lateinit var binding: ActivitySplashAitruckBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_aitruck)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val binding = ActivitySplashAitruckBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Glide.with(this)
            .load(R.drawable.splash_ai_truck)
            .into(binding.videoSplash)

        loadChatBot()

    }

    fun loadChatBot(){
        Handler().postDelayed({
            intent = Intent(this, ChatBot::class.java)
            startActivity(intent)
            finish()
        }, 4800)
    }




}