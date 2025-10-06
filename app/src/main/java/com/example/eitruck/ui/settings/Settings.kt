package com.example.eitruck.ui.settings

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.Manifest
import com.example.eitruck.R
import com.example.eitruck.databinding.ActivitySettingsBinding
import com.example.eitruck.ui.login.Login
import com.example.eitruck.ui.profile.Profile

class Settings : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val REQUEST_CAMERA = 100
    private val REQUEST_NOTIFICATIONS = 200

    private val prefs by lazy {
        getSharedPreferences("settings_prefs", MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val sysBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(v.paddingLeft, v.paddingTop, v.paddingRight, sysBars.bottom)
            insets
        }

        binding.backSettingsToProfile.setOnClickListener {
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
        }

        binding.button4.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }

        setupSwitchListeners()
        syncPermissions()
    }

    private fun setupSwitchListeners() {
        // Som → apenas SharedPreferences
        binding.som.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("sound_enabled", isChecked).apply()
        }

        // Notificações
        binding.notificacoes.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (ContextCompat.checkSelfPermission(
                            this, Manifest.permission.POST_NOTIFICATIONS
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                            REQUEST_NOTIFICATIONS
                        )
                    }
                }
            } else {
                // não dá pra revogar via código → só salva estado
                prefs.edit().putBoolean("notifications_enabled", false).apply()
            }
        }

        // Câmera
        binding.camera.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (ContextCompat.checkSelfPermission(
                        this, Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.CAMERA),
                        REQUEST_CAMERA
                    )
                }
            } else {
                prefs.edit().putBoolean("camera_enabled", false).apply()
            }
        }
    }

    private fun syncPermissions() {
        // Som → recupera do SharedPreferences
        binding.som.isChecked = prefs.getBoolean("sound_enabled", true)

        // Notificações
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            binding.notificacoes.isChecked = granted
            prefs.edit().putBoolean("notifications_enabled", granted).apply()
        } else {
            binding.notificacoes.isChecked = true
        }

        // Câmera
        val cameraGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        binding.camera.isChecked = cameraGranted
        prefs.edit().putBoolean("camera_enabled", cameraGranted).apply()
    }

    // Resultado das permissões
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CAMERA) {
            val granted = grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
            binding.camera.isChecked = granted
            prefs.edit().putBoolean("camera_enabled", granted).apply()
        }

        if (requestCode == REQUEST_NOTIFICATIONS) {
            val granted = grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
            binding.notificacoes.isChecked = granted
            prefs.edit().putBoolean("notifications_enabled", granted).apply()
        }
    }
}