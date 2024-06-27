package com.jdccmobile.costofliving.ui.models

import android.os.Parcelable
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
        com.jdccmobile.domain.model.Place.City(
            cityName = cityName,
            countryName = countryName,
        )
    }

    is PlaceUi.Country -> {
        com.jdccmobile.domain.model.Place.Country(
            countryName = countryName,
        )
    }
}
