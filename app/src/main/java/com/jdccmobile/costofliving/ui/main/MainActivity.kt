package com.jdccmobile.costofliving.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.jdccmobile.costofliving.R
import com.jdccmobile.costofliving.ui.intro.IntroActivity

class MainActivity : AppCompatActivity() {

    companion object {
        const val HALF_SECOND = 500L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        splashScreen.setKeepOnScreenCondition { true }

        val intent = Intent(this, IntroActivity::class.java)
        startActivity(intent)
        finish()
    }
}