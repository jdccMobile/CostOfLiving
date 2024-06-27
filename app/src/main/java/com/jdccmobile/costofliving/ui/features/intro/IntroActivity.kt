package com.jdccmobile.costofliving.ui.features.intro

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import com.jdccmobile.costofliving.R
import com.jdccmobile.costofliving.common.PermissionCheckerImpl
import com.jdccmobile.costofliving.common.PermissionRequester
import com.jdccmobile.costofliving.common.PlayServicesLocationDataSourceImpl
import com.jdccmobile.costofliving.common.app
import com.jdccmobile.costofliving.databinding.ActivityIntroBinding
import com.jdccmobile.costofliving.ui.features.home.HomeActivity
import com.jdccmobile.costofliving.ui.features.main.MainActivity.Companion.HALF_SECOND
import com.jdccmobile.costofliving.ui.features.main.dataStore
import com.jdccmobile.data.location.LocationDataSource
import com.jdccmobile.data.preferences.PreferencesDataSource
import com.jdccmobile.data.repositories.PrefsRepositoryImpl
import com.jdccmobile.data.repositories.RegionRepository
import kotlinx.coroutines.launch

class IntroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIntroBinding
    private lateinit var introSliderAdapter: IntroSliderAdapter
    private lateinit var viewModel: IntroViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val locationDataSource: LocationDataSource = PlayServicesLocationDataSourceImpl(app)
//        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(app)
        val permissionChecker = PermissionCheckerImpl(app)
        val regionRepository = RegionRepository(
            locationDataSource = locationDataSource,
            permissionChecker = permissionChecker,
        )
        val preferencesDataSource = PreferencesDataSource(app.dataStore)
        val prefsRepositoryImpl = PrefsRepositoryImpl(
            preferencesDataSource = preferencesDataSource,
        )
        viewModel =
            ViewModelProvider(
                this,
                IntroViewModelFactory(
                    activity = this,
                    regionRepository = regionRepository,
                    prefsRepository = prefsRepositoryImpl,
                ),
            ).get(IntroViewModel::class.java)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { updateUI(it.introSlidesInfo) }
            }
        }
    }

    private val coarsePermissionRequester = PermissionRequester(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION,
    )

    private fun updateUI(introSlidesInfo: List<com.jdccmobile.domain.model.IntroSlide>) {
        introSliderAdapter = IntroSliderAdapter(introSlidesInfo)
        binding.vpIntroSlider.adapter = introSliderAdapter

        setUpDots()
        setCurrentDot(0)

        binding.vpIntroSlider.registerOnPageChangeCallback(
            object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    setCurrentDot(position)
                    showGetLocationButton(position, introSlidesInfo.size)
                }
            },
        )
    }

    @Suppress("MagicNumber")
    private fun setUpDots() {
        val dots = arrayOfNulls<ImageView>(introSliderAdapter.itemCount)
        val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
        )
        layoutParams.setMargins(8, 0, 8, 0)
        for (i in dots.indices) {
            dots[i] = ImageView(applicationContext)
            dots[i].apply {
                this?.setImageDrawable(
                    ContextCompat.getDrawable(applicationContext, R.drawable.ic_inactive_intro_dot),
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
                    ContextCompat.getDrawable(applicationContext, R.drawable.ic_active_intro_dot),
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(applicationContext, R.drawable.ic_inactive_intro_dot),
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
            coarsePermissionRequester.request()
            viewModel.getCountryName()
            navigateToHome()
        }
    }

    private fun navigateToHome() {
        startActivity(Intent(this@IntroActivity, HomeActivity::class.java))
        finish()
    }
}
