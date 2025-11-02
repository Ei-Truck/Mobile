package com.example.eitruck.ui.forgot_password

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.example.eitruck.NoConnection
import com.example.eitruck.R
import com.example.eitruck.data.remote.repository.postgres.UserRepository
import com.example.eitruck.databinding.ActivityForgpassInputBinding
import com.example.eitruck.model.NotificationResponse
import com.example.eitruck.utils.NotificationManager
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.time.LocalDateTime


class ForgotpassInput : AppCompatActivity() {
    private lateinit var binding: ActivityForgpassInputBinding
    private lateinit var userRepository: UserRepository
    private lateinit var notificationManager: NotificationManager


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

        userRepository = UserRepository("")
        notificationManager = NotificationManager(this)

        binding.email.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.email.setBackgroundResource(R.drawable.layout_input)
                binding.erro.visibility = android.view.View.GONE
            }
        }

        binding.bntEnviarCod.setOnClickListener {
            binding.email.clearFocus()

            val email = binding.email.text.toString()
            getEmail(email)
        }

        binding.bntBackInput.backButton.setOnClickListener {
            finish()
        }
    }

    fun errorCredentials(text: String) {
        val error = binding.erro
        val email = binding.email

        error.visibility = android.view.View.VISIBLE
        error.text = text

        email.text.clear()
        email.setBackgroundResource(R.drawable.layout_input_erro)
    }

    fun getEmail(email: String) {
        lifecycleScope.launch {
            binding.loadingView.visibility = android.view.View.VISIBLE
            binding.progressBar.visibility = android.view.View.VISIBLE
            try {
                val response = userRepository.getUserByEmail(email)
                if (response.email != "") {
                    notificationManager.showNotification(NotificationResponse("1","Código de verificação recebido por e-mail!", "Seu código de verificação é: ${response.codigo}",
                        LocalDateTime.now().toString()
                    ))
                    val intent = Intent(this@ForgotpassInput, ForgotpassCode::class.java)
                    intent.putExtra("email", response.email)
                    intent.putExtra("codigo",response.codigo)
                    intent.putExtra("id",response.id)
                    startActivity(intent)
                    finish()
                } else {
                    errorCredentials("E-mail invalido. Tente novamente.")
                }
            } catch (e: IOException) {
                val intent = Intent(this@ForgotpassInput, NoConnection::class.java)
                startActivity(intent)
            } catch (e: HttpException) {
                if (e.code() == 404) {
                    errorCredentials("E-mail invalido. Tente novamente.")
                } else {
                    Toast.makeText(this@ForgotpassInput, "Erro de rede (${e.code()})", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.d("ForgotpassInput", "Erro inesperado: ${e.message}")
                Toast.makeText(this@ForgotpassInput, "Erro inesperado: ${e.message}", Toast.LENGTH_SHORT).show()
            }  finally {
                binding.progressBar.visibility = android.view.View.GONE
                binding.loadingView.visibility = android.view.View.GONE
            }
        }
    }
}

