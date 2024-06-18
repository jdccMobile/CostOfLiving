package com.jdccmobile.costofliving.ui.features.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jdccmobile.costofliving.R
import com.jdccmobile.costofliving.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavView: BottomNavigationView = binding.bottomNavView
        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.fragmentContainer,
        ) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNavView.setupWithNavController(navController)
    }
}
