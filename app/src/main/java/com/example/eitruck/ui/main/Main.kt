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
import com.example.eitruck.ui.login.Login

class Main : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        val loginSave = LoginSave(this)
        val prefes = loginSave.getPrefes()
        viewModel.setToken(loginSave.getToken().toString())

        binding.userName.text = prefes.getString("user_name", "")

        Glide.with(this)
            .load(prefes.getString("url_photo", ""))
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(binding.profileImage)

        viewModel.getUser(prefes.getInt("user_id", -1))

        viewModel.user.observe(this) { user ->
            if (user != null) {
                val parts = user.nomeCompleto.split(" ")
                val displayName = if (parts.size > 1) "${parts.first()} ${parts.last()}" else parts[0]

                binding.userName.text = displayName

                Glide.with(this)
                    .load(user.urlFoto)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(binding.profileImage)

                prefes.edit().apply {
                    putString("user_name", user.nomeCompleto)
                    putString("url_photo", user.urlFoto)
                    apply()
                }
            }
        }

        loadFragment(HomeFragment())
        binding.bottomNavigation.menu.findItem(R.id.nav_home)
            .setIcon(R.drawable.ic_icon_home_fill)

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
        supportFragmentManager.beginTransaction().replace(R.id.frame, fragment).commit()
    }

    private fun tirarFill(int: Int) {
        when (int) {
            R.id.nav_home -> {
                binding.bottomNavigation.menu.findItem(R.id.nav_home)
                    .setIcon(R.drawable.ic_icon_home_fill)
                binding.bottomNavigation.menu.findItem(R.id.nav_travel)
                    .setIcon(R.drawable.ic_icon_travel)
                binding.bottomNavigation.menu.findItem(R.id.nav_dash)
                    .setIcon(R.drawable.ic_icon_dash)
            }
            R.id.nav_travel -> {
                binding.bottomNavigation.menu.findItem(R.id.nav_home)
                    .setIcon(R.drawable.ic_icon_home)
                binding.bottomNavigation.menu.findItem(R.id.nav_travel)
                    .setIcon(R.drawable.ic_icon_travel_fill)
                binding.bottomNavigation.menu.findItem(R.id.nav_dash)
                    .setIcon(R.drawable.ic_icon_dash)
            }
            else -> {
                binding.bottomNavigation.menu.findItem(R.id.nav_home)
                    .setIcon(R.drawable.ic_icon_home)
                binding.bottomNavigation.menu.findItem(R.id.nav_travel)
                    .setIcon(R.drawable.ic_icon_travel)
                binding.bottomNavigation.menu.findItem(R.id.nav_dash)
                    .setIcon(R.drawable.ic_icon_dash_fill)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val prefes = LoginSave(this).getPrefes()
        val urlFoto = prefes.getString("url_photo", null)
        val nome = prefes.getString("user_name", null)

        nome?.let {
            val parts = it.split(" ")
            binding.userName.text = if (parts.size > 1) "${parts.first()} ${parts.last()}" else parts[0]
        }

        urlFoto?.let {
            Glide.with(this)
                .load(it)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(binding.profileImage)
        }

        val userId = prefes.getInt("user_id", -1)
        if (userId != -1) viewModel.getUser(userId)

         if(!LoginSave(this).isTokenValid()){
             LoginSave(this).clearToken()
             val intent = Intent(this, Login::class.java)
             startActivity(intent)
             finish()
         }
    }

    fun showLoading(show: Boolean) {
        binding.loadingView.visibility = if (show) View.VISIBLE else View.GONE
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }
}
