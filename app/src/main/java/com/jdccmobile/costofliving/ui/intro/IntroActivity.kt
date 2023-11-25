package com.jdccmobile.costofliving.ui.intro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.viewpager2.widget.ViewPager2
import com.jdccmobile.costofliving.R
import com.jdccmobile.costofliving.data.IntroSlidesProvider
import com.jdccmobile.costofliving.databinding.ActivityIntroBinding
import com.jdccmobile.costofliving.ui.main.MainActivity.Companion.HALF_SECOND

class IntroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIntroBinding
    private lateinit var introSliderAdapter: IntroSliderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                setOnClickListener { Log.i("JDJD", "Intent") }
            }
        } else {
            binding.btLocationSlide.visibility = View.GONE
        }
    }


}