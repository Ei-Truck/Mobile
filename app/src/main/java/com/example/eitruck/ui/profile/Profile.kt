package com.example.eitruck.ui.profile

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.eitruck.R
import com.example.eitruck.data.local.LoginSave
import com.example.eitruck.data.remote.repository.postgres.UserRepository
import com.example.eitruck.databinding.ActivityProfileBinding
import com.example.eitruck.ui.login.Login
import com.example.eitruck.ui.main.ProfileViewModel
import com.example.eitruck.ui.settings.Settings
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException

class Profile : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private var photoUri: Uri? = null

    private val viewModel: ProfileViewModel by viewModels()


    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {

            enviarFotoParaApi(it)
        }
    }

    private val takePhotoLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success && photoUri != null) {
            enviarFotoParaApi(photoUri!!)
        }
    }

    private val requestCameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            openCameraInternal()
        } else {
            Toast.makeText(this, "Permissão de câmera negada", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val sysBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(v.paddingLeft, v.paddingTop, v.paddingRight, sysBars.bottom)
            insets
        }

        val login = LoginSave(this)
        val savedUrl = login.getPrefes().getString("url_photo", null)
        savedUrl?.let {
            Glide.with(this)
                .load(it)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(binding.imgProfile)
        }

        val name = login.getPrefes().getString("user_name", "")
        val email = login.getPrefes().getString("user_email", "")
        val phone = login.getPrefes().getString("user_phone", "")

        binding.userName.text = name
        binding.userEmail.text = email
        binding.userPhone.text = phone

        viewModel.setToken(login.getToken().toString())
        viewModel.getUser(login.getPrefes().getInt("user_id", -1))

        viewModel.user.observe(this) { user ->
            user?.let {
                binding.userName.text = it.nomeCompleto
                binding.userEmail.text = it.email
                binding.userPhone.text = it.telefone

                val prefsEditor = login.getPrefes().edit()
                prefsEditor.putString("user_name", it.nomeCompleto)
                prefsEditor.putString("user_email", it.email)
                prefsEditor.putString("user_phone", it.telefone)
                prefsEditor.apply()
            }

            Toast.makeText(this, login.getPrefes().getString("user_name", ""), Toast.LENGTH_SHORT).show()
        }



        binding.backProfileToMain.setOnClickListener {
            finish()
        }

        binding.buttonSettings.setOnClickListener {
            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
        }

        binding.btnChangePhoto.setOnClickListener {
            showImageOptions()
        }

        binding.buttonPoliticaPrivacidade.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.modal_politica_privacidade)

            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(true)

            dialog.show()
        }

        binding.btnLogout.setOnClickListener {
            val dialog: Dialog = Dialog(this)
            dialog.setContentView(R.layout.modal_logout)

            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.window?.setGravity(Gravity.CENTER)
            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(true)
            dialog.show()

            dialog.findViewById<Button>(R.id.btn_exit).setOnClickListener {
                LoginSave(this).clearToken()
                val intent = Intent(this, Login::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }

            dialog.findViewById<Button>(R.id.btn_cancel).setOnClickListener {
                dialog.dismiss()
            }


        }
    }

    private fun showImageOptions() {
        val bottomSheet = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_image_options, null)
        bottomSheet.setContentView(view)

        view.findViewById<LinearLayout>(R.id.optionGallery).setOnClickListener {
            pickImageLauncher.launch("image/*")
            bottomSheet.dismiss()
        }

        view.findViewById<LinearLayout>(R.id.optionCamera).setOnClickListener {
            openCamera()
            bottomSheet.dismiss()
        }

        bottomSheet.show()
    }

    private fun openCamera() {
        requestCameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
    }

    private fun openCameraInternal() {
        val photoFile = File.createTempFile("profile_", ".jpg", cacheDir)
        photoUri = FileProvider.getUriForFile(
            this,
            "${packageName}.provider",
            photoFile
        )
        takePhotoLauncher.launch(photoUri)
    }

    private fun enviarFotoParaApi(uri: Uri) {
        val login = LoginSave(this)
        val userRepo = UserRepository(login.getToken().toString())
        val userId = login.getPrefes().getInt("user_id", -1)

        lifecycleScope.launch {
            binding.loadingView.visibility = android.view.View.VISIBLE
            binding.progressBar.visibility = android.view.View.VISIBLE
            try {
                val tempFile = createTempFileFromUri(uri)

                val requestBody = tempFile.asRequestBody("image/*".toMediaTypeOrNull())
                val multipart = MultipartBody.Part.createFormData("file", "profile_$userId.jpg", requestBody)

                val response = userRepo.uploadPhoto(userId, multipart)

                response.urlFoto?.let { url ->
                    Glide.with(this@Profile)
                        .load(url)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(binding.imgProfile)

                    val prefsEditor = login.getPrefes().edit()
                    prefsEditor.putString("url_photo", url)
                    prefsEditor.apply()
                }

                Toast.makeText(this@Profile, "Foto atualizada com sucesso!", Toast.LENGTH_SHORT).show()
                tempFile.delete()

            } catch (e: retrofit2.HttpException) {
                Toast.makeText(this@Profile, "Erro HTTP ao enviar foto: ${e.code()}", Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                Toast.makeText(this@Profile, "Erro ao processar imagem", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this@Profile, "Erro ao enviar foto: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                binding.loadingView.visibility = android.view.View.GONE
                binding.progressBar.visibility = android.view.View.GONE
            }
        }
    }

    private fun createTempFileFromUri(uri: Uri): File {
        val inputStream = contentResolver.openInputStream(uri)
            ?: throw IOException("Não foi possível abrir o URI: $uri")
        val tempFile = File.createTempFile("profile_", ".jpg", cacheDir)
        inputStream.use { input ->
            tempFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        return tempFile
    }
}
