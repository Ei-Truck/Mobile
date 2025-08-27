package com.example.eitruck.ui.forgot_password

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.eitruck.databinding.ActivityForgotpassCodeBinding

class ForgotpassCode : AppCompatActivity() {

    private lateinit var binding: ActivityForgotpassCodeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityForgotpassCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Referências aos EditTexts (como você já tinha)
        val editTextCode1 = binding.editTextCode1
        val editTextCode2 = binding.editTextCode2
        val editTextCode3 = binding.editTextCode3
        val editTextCode4 = binding.editTextCode4

        // Configurar os listeners
        setupOtpListeners(editTextCode1, null, editTextCode2)
        setupOtpListeners(editTextCode2, editTextCode1, editTextCode3)
        setupOtpListeners(editTextCode3, editTextCode2, editTextCode4)
        setupOtpListeners(editTextCode4, editTextCode3, null)


        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.bntBackCode.backButton.setOnClickListener {
            finish()
        }

        binding.bntConfirm.setOnClickListener {
            val intent = Intent(this, ForgotpassChange::class.java)
            startActivity(intent)
        }
    }

    private fun setupOtpListeners(
        currentField: EditText,
        previousField: EditText?,
        nextField: EditText?
    ) {
        currentField.addTextChangedListener(OtpTextWatcher(currentField, previousField, nextField))
        currentField.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
                if (currentField.text.isEmpty() && previousField != null) {
                    previousField.requestFocus()
                    previousField.setSelection(previousField.text.length)
                    return@setOnKeyListener true
                }
            }
            false
        }
    }


    inner class OtpTextWatcher(
        private val currentView: EditText,
        private val previousView: EditText?,
        private val nextView: EditText?
    ) : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            val text = editable.toString()
            if (text.length == 1 && nextView != null) {
                nextView.requestFocus()
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (before > 0 && count == 0 && s.toString().isEmpty() && previousView != null) {
                previousView.requestFocus()
                previousView.setSelection(previousView.text.length)
            }
        }
    }
}
