package com.jdccmobile.costofliving.ui.models

import android.os.Parcelable
import com.jdccmobile.costofliving.domain.models.Place
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class PlaceUi : Parcelable {
    abstract val countryName: String

    data class City(
        override val countryName: String,
        val cityName: String,
    ) : PlaceUi()

    data class Country(
        override val countryName: String,
    ) : PlaceUi()
}

fun PlaceUi.toDomain() = when (this) {
    is PlaceUi.City -> {
        Place.City(
            cityName = cityName,
            countryName = countryName,
        )
    }

    is PlaceUi.Country -> {
        Place.Country(
            countryName = countryName,
        )
    }
}
