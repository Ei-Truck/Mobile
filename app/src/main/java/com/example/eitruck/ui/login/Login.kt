package com.example.eitruck.ui.login

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.eitruck.R
import com.example.eitruck.databinding.ActivityLoginBinding
import com.example.eitruck.model.LoginRequest
import com.example.eitruck.repository.postgres.LoginRepository
import com.example.eitruck.ui.main.Main
import com.example.eitruck.ui.forgot_password.ForgotpassInput
import com.example.eitruck.worker.LoginSave
import kotlinx.coroutines.launch
import retrofit2.HttpException

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val loginRepository = LoginRepository()

        binding.fogotPaswword.setOnClickListener {
            val intent = Intent(this, ForgotpassInput::class.java)
            startActivity(intent)
        }

        val email = binding.email
        val password = binding.password

        binding.buttonEntrar.setOnClickListener {
            val email = email.text.toString()
            val password = password.text.toString()
            login(email, password, loginRepository)
        }

        email.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                email.setBackgroundResource(R.drawable.layout_input)
                binding.erro.visibility = android.view.View.GONE
            }
        }

        password.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                password.setBackgroundResource(R.drawable.layout_input)
                binding.erro.visibility = android.view.View.GONE
            }
        }

    }

    fun login(email: String, password: String, loginRepository: LoginRepository){
        val load = binding.progressBar

        lifecycleScope.launch {
            try {
                load.visibility = android.view.View.VISIBLE

                val response = loginRepository.login(LoginRequest(email, password))
                if (response == null){
                    erroCredential("Endereço de email e/ou senha invalido(s). Tente novamente")
                } else{
                    val loginSave = LoginSave(this@Login, response)
                    loginSave.saveToken()
                    val intent = Intent(this@Login, Main::class.java)
                    startActivity(intent)
                    finish()
                }
            } catch (e: HttpException) {
                Toast.makeText(this@Login, "Erro de rede (${e.code()})", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this@Login, "Erro inesperado: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                load.visibility = android.view.View.GONE
            }
        }
    }

    fun erroCredential(text: String){
        val error = binding.erro
        val email = binding.email
        val password = binding.password

        error.visibility = android.view.View.VISIBLE
        error.text = text

        email.text.clear()
        password.text.clear()

        email.setBackgroundResource(R.drawable.layout_input_erro)
        password.setBackgroundResource(R.drawable.layout_input_erro)

    }
}