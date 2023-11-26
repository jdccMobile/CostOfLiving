package com.jdccmobile.costofliving.ui.intro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.jdccmobile.costofliving.R
import com.jdccmobile.costofliving.data.IntroSlidesProvider
import com.jdccmobile.costofliving.databinding.ActivityIntroBinding
import com.jdccmobile.costofliving.domain.RegionRepository
import com.jdccmobile.costofliving.ui.home.HomeActivity
import com.jdccmobile.costofliving.ui.main.MainActivity.Companion.COUNTRY_NAME
import com.jdccmobile.costofliving.ui.main.MainActivity.Companion.HALF_SECOND
import com.jdccmobile.costofliving.ui.main.dataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class IntroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIntroBinding
    private lateinit var introSliderAdapter: IntroSliderAdapter
    private lateinit var regionRepository: RegionRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        regionRepository = RegionRepository(this)

        val introSlideInfo = IntroSlidesProvider(this).getIntroSlides()
        val introSlideInfoSize = introSlideInfo.size
        introSliderAdapter = IntroSliderAdapter(introSlideInfo)
        binding.vpIntroSlider.adapter = introSliderAdapter

        setUpDots()
        setCurrentDot(0)

        binding.vpIntroSlider.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentDot(position)
                showGetLocationButton(position, introSlideInfoSize)
            }
        })

    }

    private fun setUpDots() {
        val dots = arrayOfNulls<ImageView>(introSliderAdapter.itemCount)
        val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(8, 0, 8, 0)
        for (i in dots.indices) {
            dots[i] = ImageView(applicationContext)
            dots[i].apply {
                this?.setImageDrawable(
                    ContextCompat.getDrawable(applicationContext, R.drawable.ic_inactive_intro_dot)
                )
                this?.layoutParams = layoutParams
            }
            binding.llDotsIndicators.addView(dots[i])
        }
    }

    private fun setCurrentDot(index: Int) {
        val childCount = binding.llDotsIndicators.childCount
        for (i in 0 until childCount) {
            val imageView = binding.llDotsIndicators[i] as ImageView
            if (i == index) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(applicationContext, R.drawable.ic_active_intro_dot)
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(applicationContext, R.drawable.ic_inactive_intro_dot)
                )
            }
        }
    }

    private fun showGetLocationButton(position: Int, maxSlides: Int) {
        if (position == maxSlides - 1) {
            binding.btLocationSlide.apply {
                visibility = View.VISIBLE
                alpha = 0f
                animate().alpha(1f).duration = HALF_SECOND
                setOnClickListener { askForLocation() }
            }
        } else {
            binding.btLocationSlide.visibility = View.GONE
        }
    }

    private fun askForLocation() {
        lifecycleScope.launch {
            val countryCode = regionRepository.findLastRegion()
            val countryName = Locale("", countryCode).getDisplayCountry(Locale("EN"))
            savePreferences(countryName)
            Log.i("JDJD", "countryName: $countryName")
            withContext(Dispatchers.Main) {
                startActivity(Intent(this@IntroActivity, HomeActivity::class.java))
            }
        }
    }

    private suspend fun savePreferences(countryName: String) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey(COUNTRY_NAME)] = countryName
        }
    }

}