package com.jdccmobile.costofliving.domain.models

import com.jdccmobile.costofliving.ui.models.PlaceUi

sealed class Place {
    abstract val countryName: String

    data class City(
        override val countryName: String,
        val cityName: String,
    ) : Place()

    data class Country(
        override val countryName: String,
    ) : Place()
}

fun List<Place.City>.toUi() = map {
    PlaceUi.City(
        cityName = it.cityName,
        countryName = it.countryName,
    )
}
