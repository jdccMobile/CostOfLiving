package com.jdccmobile.costofliving.ui.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.jdccmobile.costofliving.R
import com.jdccmobile.costofliving.data.CostInfoRepository
import com.jdccmobile.costofliving.domain.usecases.RequestUserCountryPrefsUC
import com.jdccmobile.costofliving.ui.common.app
import com.jdccmobile.costofliving.ui.home.HomeActivity
import com.jdccmobile.costofliving.ui.intro.IntroActivity
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

const val PREFERENCES = "preferences"
//val Context.dataStore by preferencesDataStore(name = PREFERENCES)

class MainActivity : AppCompatActivity() {

    companion object {
        const val HALF_SECOND = 500L
        const val DEFAULT_COUNTRY_CODE = "es"
    }

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        splashScreen.setKeepOnScreenCondition { true }


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.state.collect{ state ->
                    state.countryName?.let { navigateTo(it) }
                }
            }
        }
    }

    private fun navigateTo(countryName: String){
        val intentActivity = if (countryName != "") HomeActivity::class.java else IntroActivity::class.java
        startActivity(Intent(this@MainActivity, intentActivity))
        finish()
    }

}