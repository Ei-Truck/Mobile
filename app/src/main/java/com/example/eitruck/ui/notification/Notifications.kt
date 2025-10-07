package com.example.eitruck.ui.notification

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.eitruck.R
import com.example.eitruck.databinding.ActivityNotificationsBinding
import kotlinx.coroutines.launch

class Notifications : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationsBinding
    private val viewMode: NotificationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_notifications)
        val rootView = findViewById<android.view.View>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding = ActivityNotificationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backNotificationToHome.setOnClickListener {
            finish()
        }

        viewMode.carregandoLiveData.observe(this) { carregando ->
            if (carregando) {
                binding.progressBar.visibility = android.view.View.VISIBLE
            } else {
                binding.progressBar.visibility = android.view.View.GONE
            }
        }

        viewMode.notifications.observe(this) { notifications ->
            val adapter = NotificationAdapter(notifications)
            binding.notificationsRecycler.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
            binding.notificationsRecycler.adapter = adapter
        }

        if (viewMode.notifications.value.isNullOrEmpty()) {
            lifecycleScope.launch {
                viewMode.getNotifications("1")
            }
        }

    }
}
