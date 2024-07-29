package com.jdccmobile.costofliving.ui.features.intro

import android.content.Context
import com.jdccmobile.costofliving.R
import com.jdccmobile.domain.model.IntroSlide

class IntroSlidesProvider(context: Context) {
    fun getIntroSlides() = introSlides

    private val introSlides = listOf(
        IntroSlide(
            context.getString(R.string.app_name),
            context.getString(R.string.cost_of_living_des),
            R.drawable.im_world,
        ),
        IntroSlide(
            context.getString(R.string.cities),
            context.getString(R.string.cities_des),
            R.drawable.im_town,
        ),
        IntroSlide(
            context.getString(R.string.favorites),
            context.getString(R.string.favorites_des),
            R.drawable.im_saved_town,
        ),
        IntroSlide(
            context.getString(R.string.location),
            context.getString(R.string.location_des),
            R.drawable.im_current_location,
        ),
    )
}
