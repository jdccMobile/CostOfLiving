package com.jdccmobile.costofliving.ui.features.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.jdccmobile.costofliving.R
import com.jdccmobile.costofliving.ui.features.home.HomeActivity
import com.jdccmobile.costofliving.ui.features.intro.IntroActivity
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

// const val PREFERENCES = "preferences"
// val Context.dataStore by preferencesDataStore(name = PREFERENCES)

class MainActivity : AppCompatActivity() {
    companion object {
        const val HALF_SECOND = 500L
        const val DEFAULT_COUNTRY_CODE = "pt"
    }

    // TODO utilizar la linea comentada
//    private val viewModel: MainViewModel by viewModels { MainViewModelFactory(dataStore) }
//    private lateinit var viewModel: MainViewModel
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        splashScreen.setKeepOnScreenCondition { true }

//        val preferencesDataSource = PreferencesDataSource(app.dataStore)
//        val prefsRepository = PrefsRepositoryImpl(
//            preferencesDataSource = preferencesDataSource,
//        )
//        viewModel = ViewModelProvider(
//            this,
//            MainViewModelFactory(
//                GetUserCountryPrefsUseCase(prefsRepository),
//            ),
//        ).get(MainViewModel::class.java)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    state.countryName?.let { navigateTo(it) }
                }
            }
        }
    }

    private fun navigateTo(countryName: String) {
        val intentActivity = if (countryName != "") {
            HomeActivity::class.java
        } else {
            IntroActivity::class.java
        }
        startActivity(Intent(this@MainActivity, intentActivity))
        finish()
    }
}
