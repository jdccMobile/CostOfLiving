package com.jdccmobile.costofliving.ui.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.jdccmobile.costofliving.R
import com.jdccmobile.costofliving.ui.home.HomeActivity
import com.jdccmobile.costofliving.ui.intro.IntroActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

val Context.dataStore by preferencesDataStore(name = "preferences")

class MainActivity : AppCompatActivity() {

    companion object {
        const val HALF_SECOND = 500L
        const val COUNTRY_NAME = "country_name"
        const val DEFAULT_COUNTRY_CODE = "es"
        const val DEFAULT_COUNTRY_NAME = "Spain"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        splashScreen.setKeepOnScreenCondition { true }

        lifecycleScope.launch(Dispatchers.IO) {
            val countryName = getPreferences(COUNTRY_NAME)
            Log.i("JDJD", "countryName: $countryName")
            withContext(Dispatchers.Main) {
                val intentActivity = if (countryName != "") HomeActivity::class.java else IntroActivity::class.java
                startActivity(Intent(this@MainActivity, intentActivity))
                finish()
            }
        }
    }

    private suspend fun getPreferences(key: String): String {
        val preferences = dataStore.data.first()
        return preferences[stringPreferencesKey(key)] ?: ""
    }
}