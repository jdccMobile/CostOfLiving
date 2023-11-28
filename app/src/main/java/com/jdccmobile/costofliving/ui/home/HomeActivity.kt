package com.jdccmobile.costofliving.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.jdccmobile.costofliving.R
import com.jdccmobile.costofliving.databinding.ActivityHomeBinding
import com.jdccmobile.costofliving.ui.home.favorites.FavoritesFragment
import com.jdccmobile.costofliving.ui.home.search.SearchFragment
import com.jdccmobile.costofliving.ui.main.MainActivity.Companion.COUNTRY_NAME
import com.jdccmobile.costofliving.ui.main.MainActivity.Companion.DEFAULT_COUNTRY_NAME
import com.jdccmobile.costofliving.ui.main.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var countryName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(SearchFragment())



        lifecycleScope.launch { countryName = getPreferences(COUNTRY_NAME) }

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

    private suspend fun getPreferences(key: String): String {
        return dataStore.data.first()[stringPreferencesKey(key)] ?: DEFAULT_COUNTRY_NAME
    }
}