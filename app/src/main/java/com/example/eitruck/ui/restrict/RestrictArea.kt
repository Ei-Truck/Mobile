package com.example.eitruck.ui.restrict

import android.os.Bundle
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.eitruck.R
import com.example.eitruck.databinding.ActivityRestrictAreaBinding

class RestrictArea : AppCompatActivity() {

    private lateinit var binding : ActivityRestrictAreaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRestrictAreaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding.backToSettings.setOnClickListener {
            finish()
        }

        val webView = binding.webView

        webView.webViewClient = WebViewClient()

        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true

        webView.loadUrl("https://app.powerbi.com/view?r=eyJrIjoiYTAwY2Y4YTAtNzM4NS00NTQ4LTkxYzctMzJlOWVlN2E4M2E0IiwidCI6ImIxNDhmMTRjLTIzOTctNDAyYy1hYjZhLTFiNDcxMTE3N2FjMCJ9")
    }
}