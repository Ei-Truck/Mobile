package com.example.eitruck.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.eitruck.R
import com.example.eitruck.databinding.ActivityMainBinding
import com.example.eitruck.ui.SplashAITruck
import com.example.eitruck.ui.dash.DashFragment
import com.example.eitruck.ui.home.HomeFragment
import com.example.eitruck.ui.notification.Notifications
import com.example.eitruck.ui.profile.Profile
import com.example.eitruck.ui.travel.TravelFragment
import com.example.eitruck.data.local.LoginSave

class Main : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupInsets()
        loadUserData()
        setupBottomNavigation()
        setupClickListeners()
        loadFragment(HomeFragment())
    }

    private fun setupInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.bottomNavigation) { v, insets ->
            val sysBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(v.paddingLeft, v.paddingTop, v.paddingRight, sysBars.bottom)
            v.setBackgroundColor(getColor(R.color.colorPrimaryDark))
            insets
        }

        window.navigationBarColor = getColor(R.color.colorPrimaryDark)
        window.statusBarColor = getColor(R.color.colorPrimaryDark)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val sysBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(sysBars.left, sysBars.top, sysBars.right, 0)
            insets
        }
    }

    private fun loadUserData() {
        val loginSave = LoginSave(this)
        val prefes = loginSave.getPrefes()

        viewModel.setToken(loginSave.getToken().toString())

        val urlFoto = prefes.getString("url_photo", null)
        urlFoto?.let {
            Glide.with(this)
                .load(it)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(binding.profileImage)
        }

        val fullName = prefes.getString("user_name", "")
        binding.userName.text = getDisplayName(fullName)

        val userId = prefes.getInt("user_id", -1)
        if (userId != -1) {
            viewModel.getUser(userId)
        }

        viewModel.user.observe(this) { user ->
            user?.let {
                prefes.edit().apply {
                    putString("user_name", it.nomeCompleto)
                    putString("url_photo", it.urlFoto)
                    apply()
                }

                binding.userName.text = getDisplayName(it.nomeCompleto)

                Glide.with(this)
                    .load(it.urlFoto)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(binding.profileImage)
            }
        }
    }

    private fun getDisplayName(fullName: String?): String {
        if (fullName.isNullOrBlank()) return ""
        val parts = fullName.split(" ")
        return if (parts.size > 1) "${parts.first()} ${parts.last()}" else fullName
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.menu.findItem(R.id.nav_home).setIcon(R.drawable.ic_icon_home_fill)

        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    tirarFill(it.itemId)
                    loadFragment(HomeFragment())
                }
                R.id.nav_travel -> {
                    tirarFill(it.itemId)
                    loadFragment(TravelFragment())
                }
                R.id.nav_dash -> {
                    tirarFill(it.itemId)
                    loadFragment(DashFragment())
                }
            }
            true
        }
    }

    private fun setupClickListeners() {
        binding.chatBot.setOnClickListener {
            startActivity(Intent(this, SplashAITruck::class.java))
        }
        binding.notification.setOnClickListener {
            startActivity(Intent(this, Notifications::class.java))
        }
        binding.profileImage.setOnClickListener {
            startActivity(Intent(this, Profile::class.java))
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, fragment)
            .commit()
    }

    private fun tirarFill(int: Int) {
        binding.bottomNavigation.menu.findItem(R.id.nav_home)
            .setIcon(if (int == R.id.nav_home) R.drawable.ic_icon_home_fill else R.drawable.ic_icon_home)
        binding.bottomNavigation.menu.findItem(R.id.nav_travel)
            .setIcon(if (int == R.id.nav_travel) R.drawable.ic_icon_travel_fill else R.drawable.ic_icon_travel)
        binding.bottomNavigation.menu.findItem(R.id.nav_dash)
            .setIcon(if (int == R.id.nav_dash) R.drawable.ic_icon_dash_fill else R.drawable.ic_icon_dash)
    }

    override fun onResume() {
        super.onResume()
        val loginSave = LoginSave(this)
        val prefes = loginSave.getPrefes()

        val urlFoto = prefes.getString("url_photo", null)
        urlFoto?.let {
            Glide.with(this)
                .load(it)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(binding.profileImage)
        }

        val fullName = prefes.getString("user_name", "")
        binding.userName.text = getDisplayName(fullName)

        val userId = prefes.getInt("user_id", -1)
        if (userId != -1) viewModel.getUser(userId)
    }

    fun showLoading(show: Boolean) {
        val overlay = binding.loadingView
        val progress = binding.progressBar
        overlay.visibility = if (show) View.VISIBLE else View.GONE
        progress.visibility = if (show) View.VISIBLE else View.GONE
    }
}
