package com.example.eitruck

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.example.eitruck.R
import com.example.eitruck.databinding.ActivityMainBinding
import com.example.eitruck.ui.dash.DashFragment
import com.example.eitruck.ui.home.HomeFragment
import com.example.eitruck.ui.travel.TravelFragment

class Main : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.bottomNavigation) { v, insets ->
            val sysBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(v.paddingLeft, v.paddingTop, v.paddingRight, sysBars.bottom)
            v.setBackgroundColor(getColor(R.color.colorPrimaryDark)) // cor de fundo
            insets
        }

        window.navigationBarColor = getColor(R.color.colorPrimaryDark)
        window.statusBarColor = getColor(R.color.colorPrimaryDark)




        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val sysBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(sysBars.left, sysBars.top, sysBars.right, 0)
            insets
        }

        loadFragment(HomeFragment())

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

    private fun loadFragment(fragment: androidx.fragment.app.Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, fragment)
            .commit()
    }

    private fun tirarFill(int: Int) {
        if (int == R.id.nav_home) {
            binding.bottomNavigation.menu.findItem(R.id.nav_home).setIcon(R.drawable.ic_icon_home_fill)
            binding.bottomNavigation.menu.findItem(R.id.nav_travel).setIcon(R.drawable.ic_icon_travel)
            binding.bottomNavigation.menu.findItem(R.id.nav_dash).setIcon(R.drawable.ic_icon_dash)
        } else if(int == R.id.nav_travel) {
            binding.bottomNavigation.menu.findItem(R.id.nav_home).setIcon(R.drawable.ic_icon_home)
            binding.bottomNavigation.menu.findItem(R.id.nav_travel).setIcon(R.drawable.ic_icon_travel_fill)
            binding.bottomNavigation.menu.findItem(R.id.nav_dash).setIcon(R.drawable.ic_icon_dash)
        } else{
            binding.bottomNavigation.menu.findItem(R.id.nav_home).setIcon(R.drawable.ic_icon_home)
            binding.bottomNavigation.menu.findItem(R.id.nav_travel).setIcon(R.drawable.ic_icon_travel)
            binding.bottomNavigation.menu.findItem(R.id.nav_dash).setIcon(R.drawable.ic_icon_dash_fill)
        }
    }
}