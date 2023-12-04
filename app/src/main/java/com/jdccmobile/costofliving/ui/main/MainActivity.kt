package com.jdccmobile.costofliving.ui.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.preferences.preferencesDataStore
import com.jdccmobile.costofliving.R
import com.jdccmobile.costofliving.ui.home.HomeActivity
import com.jdccmobile.costofliving.ui.intro.IntroActivity

const val PREFERENCES = "preferences"
val Context.dataStore by preferencesDataStore(name = PREFERENCES)

class MainActivity : AppCompatActivity() {

    companion object {
        const val HALF_SECOND = 500L
        const val COUNTRY_NAME = "country_name"
        const val DEFAULT_COUNTRY_CODE = "es"
        const val DEFAULT_COUNTRY_NAME = "Spain"
    }

    private val viewModel: MainViewModel by viewModels { MainViewModelFactory(dataStore) }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        splashScreen.setKeepOnScreenCondition { true }

        viewModel.state.observe(this){
            if(it.countryName != null) navigateTo(it.countryName)
        }
    }

    private fun navigateTo(countryName: String){
        val intentActivity = if (countryName != "") HomeActivity::class.java else IntroActivity::class.java
        startActivity(Intent(this@MainActivity, intentActivity))
        finish()
    }

}