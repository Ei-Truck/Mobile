package com.example.eitruck.ui.tratativa

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.eitruck.R

class TratativaNote : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tratativa_note)

        val editTitle = findViewById<EditText>(R.id.editTitle)
        val editBody = findViewById<EditText>(R.id.editBody)
        val btnSalvar = findViewById<Button>(R.id.button2)
        val backButton = findViewById<Button>(R.id.back_tratativa_to_travel)

        editBody.setText(intent.getStringExtra("textoExistente") ?: "")
        editTitle.setText(intent.getStringExtra("tituloExistente") ?: "")

        backButton.setOnClickListener { finish() }

        btnSalvar.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("tratativaTexto", editBody.text.toString().trim())
            resultIntent.putExtra("tratativaTitulo", editTitle.text.toString().trim())
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}
