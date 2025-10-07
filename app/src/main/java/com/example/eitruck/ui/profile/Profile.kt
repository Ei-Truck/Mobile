package com.example.eitruck.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.eitruck.ui.main.Main
import com.example.eitruck.R
import com.example.eitruck.databinding.ActivityProfileBinding
import com.example.eitruck.ui.settings.Settings
import java.io.File

class Profile : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private var photoUri: Uri? = null


    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            binding.imgProfile.setImageURI(it)
        }
    }

    private val takePhotoLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success && photoUri != null) {
            binding.imgProfile.setImageURI(photoUri)
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

        binding.backProfileToMain.setOnClickListener {
            val intent = Intent(this, Main::class.java)
            startActivity(intent)
        }

        binding.buttonSettings.setOnClickListener{
            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
        }

        binding.btnChangePhoto.setOnClickListener {
            showImageOptions()
        }
    }


    private fun showImageOptions() {
        val bottomSheet = com.google.android.material.bottomsheet.BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_image_options, null)
        bottomSheet.setContentView(view)

        // Clique para escolher da galeria
        view.findViewById<LinearLayout>(R.id.optionGallery).setOnClickListener {
            pickImageLauncher.launch("image/*")
            bottomSheet.dismiss()
        }

        // Clique para tirar foto
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
        // Cria arquivo temporário
        val photoFile = File.createTempFile("profile_", ".jpg", cacheDir)
        photoUri = FileProvider.getUriForFile(
            this,
            "${packageName}.provider",
            photoFile
        )
        takePhotoLauncher.launch(photoUri)
    }

}
