package com.example.eitruck.ui.forgot_password

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.example.eitruck.R
import com.example.eitruck.data.remote.repository.postgres.UserRepository
import com.example.eitruck.databinding.ActivityForgpassInputBinding
import kotlinx.coroutines.launch
import retrofit2.HttpException


class ForgotpassInput : AppCompatActivity() {
    private lateinit var binding: ActivityForgpassInputBinding
    private lateinit var userRepository: UserRepository


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

        binding.telefone.addTextChangedListener(object : android.text.TextWatcher {
            private var isUpdating = false

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (isUpdating) return

                var digits = s.toString().replace("[^\\d]".toRegex(), "")

                if (digits.length > 11) digits = digits.substring(0, 11)

                val formatted = buildString {
                    if (digits.isNotEmpty()) append("+")
                    if (digits.length >= 1) append(digits.substring(0, Math.min(2, digits.length)))
                    if (digits.length > 2) append(" ")
                    if (digits.length > 2) append(digits.substring(2, Math.min(6, digits.length)))
                    if (digits.length > 6) append("-")
                    if (digits.length > 6) append(digits.substring(6))
                }

                isUpdating = true
                binding.telefone.setText(formatted)
                binding.telefone.setSelection(formatted.length)
                isUpdating = false
            }

            override fun afterTextChanged(s: android.text.Editable?) {}
        })



        binding.telefone.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.telefone.setBackgroundResource(R.drawable.layout_input)
                binding.erro.visibility = android.view.View.GONE
            }
        }

        binding.bntEnviarCod.setOnClickListener {
            binding.telefone.clearFocus()

            val telefone = binding.telefone.text.toString().replace("[^\\d+]".toRegex(), "")
            getTelefone(telefone)
        }

        binding.bntBackInput.backButton.setOnClickListener {
            finish()
        }
    }

    fun errorCredentials(text: String) {
        val error = binding.erro
        val email = binding.telefone

        error.visibility = android.view.View.VISIBLE
        error.text = text

        email.text.clear()
        email.setBackgroundResource(R.drawable.layout_input_erro)
    }

    fun getTelefone(telefone: String) {
        lifecycleScope.launch {
            binding.loadingView.visibility = android.view.View.VISIBLE
            binding.progressBar.visibility = android.view.View.VISIBLE
            try {
                val response = userRepository.getUserByPhone(telefone).telefone
                if (response != "") {
                    val intent = Intent(this@ForgotpassInput, ForgotpassCode::class.java)
                    intent.putExtra("telefone", response)
                    startActivity(intent)
                    finish()
                } else {
                    errorCredentials("Telefone invalido. Tente novamente.")
                }
            } catch (e: HttpException) {
                if (e.code() == 404) {
                    errorCredentials("Telefone invalido. Tente novamente.")
                } else {
                    Toast.makeText(this@ForgotpassInput, "Erro de rede (${e.code()})", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@ForgotpassInput, "Erro inesperado: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                binding.progressBar.visibility = android.view.View.GONE
                binding.loadingView.visibility = android.view.View.GONE
            }
        }
    }
}

