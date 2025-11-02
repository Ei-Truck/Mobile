package com.example.eitruck.ui.forgot_password

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.eitruck.NoConnection
import com.example.eitruck.R
import com.example.eitruck.data.remote.repository.postgres.UserRepository
import com.example.eitruck.databinding.ActivityForgotpassChangeBinding
import com.example.eitruck.model.UserPassword
import kotlinx.coroutines.launch
import java.io.IOException

class ForgotpassChange : AppCompatActivity() {

    private lateinit var userRepository: UserRepository
    private lateinit var binding: ActivityForgotpassChangeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityForgotpassChangeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        userRepository = UserRepository("")

        val id = intent.getIntExtra("id", 0)

        binding.password.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.password.setBackgroundResource(R.drawable.layout_input)
                binding.erro.visibility = android.view.View.GONE
            }
        }

        binding.bntBackChange.backButton.setOnClickListener {
            finish()
        }

        val senha = binding.password
        val confirmSenha = binding.confirmPassword

        binding.bntChangePass.setOnClickListener {
            alterarSenha(id,senha.text.toString(),confirmSenha.text.toString())
        }

    }

    fun errorCredentials(text: String) {
        val error = binding.erro
        val senha = binding.password
        val confirmSenha = binding.confirmPassword

        error.visibility = android.view.View.VISIBLE
        error.text = text


        senha.text.clear()
        senha.setBackgroundResource(R.drawable.layout_input_erro)
        confirmSenha.text.clear()
        confirmSenha.setBackgroundResource(R.drawable.layout_input_erro)
    }

    fun alterarSenha(id: Int,senha: String, confirmSenha: String){
        val load = binding.progressBar
        val view = binding.loadingView

        if (senha == confirmSenha) {
            lifecycleScope.launch {
                load.visibility = android.view.View.VISIBLE
                view.visibility = android.view.View.VISIBLE
                try {
                    val response = userRepository.updateUser(id, UserPassword(senha))
                    if (response.nomeCompleto != "") {
                        finish()
                    } else {
                        errorCredentials("Erro ao alterar senha")
                    }
                } catch (e: IOException) {
                    val intent = Intent(this@ForgotpassChange, NoConnection::class.java)
                    startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(this@ForgotpassChange, "Erro inesperado: ${e.message}", Toast.LENGTH_SHORT).show()
                } finally {
                    load.visibility = android.view.View.GONE
                    view.visibility = android.view.View.GONE
                }
            }
        } else {
            errorCredentials("Senhas não conferem")
        }
    }
}