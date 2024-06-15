package com.jdccmobile.costofliving.data.local

import androidx.appcompat.app.AppCompatActivity
import com.jdccmobile.costofliving.R
import com.jdccmobile.costofliving.model.IntroSlide

class IntroSlidesProvider(activity: AppCompatActivity) {
    private val introSlides = listOf(
        IntroSlide(
            activity.getString(R.string.app_name),
            activity.getString(R.string.cost_of_living_des),
            R.drawable.im_world,
        ),
        IntroSlide(
            activity.getString(R.string.cities),
            activity.getString(R.string.cities_des),
            R.drawable.im_town,
        ),
        IntroSlide(
            activity.getString(R.string.favorites),
            activity.getString(R.string.favorites_des),
            R.drawable.im_saved_town,
        ),
        IntroSlide(
            activity.getString(R.string.location),
            activity.getString(R.string.location_des),
            R.drawable.im_current_location,
        ),
    )

    fun getIntroSlides() = introSlides
}
