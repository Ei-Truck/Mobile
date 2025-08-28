package com.example.eitruck.ui.forgot_password

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.eitruck.databinding.ActivityForgpassInputBinding
class ForgotpassInput : AppCompatActivity() {
    private lateinit var binding: ActivityForgpassInputBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityForgpassInputBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.bntEnviarCod.setOnClickListener {
            val intent = Intent(this, ForgotpassCode::class.java)
            startActivity(intent)
        }

        binding.bntBackInput.backButton.setOnClickListener {
            finish()
        }
    }
}

