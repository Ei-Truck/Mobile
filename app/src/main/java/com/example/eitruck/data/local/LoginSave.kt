package com.example.eitruck.data.local

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.eitruck.model.LoginResponse
import org.json.JSONObject

class LoginSave(
    private val context: Context,
    private val login: LoginResponse?= null
) {

    private val prefs by lazy {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        EncryptedSharedPreferences.create(
            context,
            "secure_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun saveToken() {
        login?.let {
            prefs.edit().apply {
                val nameParts = login.nomeCompleto.split(" ")
                val displayName = if (nameParts.size > 1) {
                    "${nameParts.first()} ${nameParts.last()}"
                } else {
                    nameParts[0]
                }

                putString("auth_token", login.token)
                putInt("user_id", login.id)
                putString("user_name", displayName)
                putString("user_email", login.email)
                putString("user_telefone", login.telefone)
                putString("user_cargo", login.cargo)
                apply()
            }
        }
    }

    fun getToken(): String? {
        return prefs.getString("auth_token", null)
    }

    fun isTokenValid(): Boolean {
        val token = getToken() ?: return false
        val parts = token.split(".")
        if (parts.size != 3) return false

        return try {
            val payloadJson = String(Base64.decode(parts[1], Base64.URL_SAFE))
            val exp = JSONObject(payloadJson).getLong("exp")
            val now = System.currentTimeMillis() / 1000
            now < exp
        } catch (e: Exception) {
            false
        }
    }

    fun getPrefes(): SharedPreferences {
        return prefs
    }


    fun clearToken() {
        prefs.edit().clear().apply()
    }


}