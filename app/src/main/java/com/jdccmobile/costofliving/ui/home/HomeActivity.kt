package com.jdccmobile.costofliving.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.jdccmobile.costofliving.R
import com.jdccmobile.costofliving.databinding.ActivityHomeBinding
import com.jdccmobile.costofliving.ui.home.favorites.FavoritesFragment
import com.jdccmobile.costofliving.ui.home.search.SearchFragment

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(SearchFragment())
        initBottomNav()

    }

    private fun initBottomNav() {
        binding.bottomNavView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.search -> replaceFragment(SearchFragment())
                R.id.favorites -> replaceFragment(FavoritesFragment())
                else -> {}
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.flFragmentContainer, fragment)
        fragmentTransaction.commit()
    }

}