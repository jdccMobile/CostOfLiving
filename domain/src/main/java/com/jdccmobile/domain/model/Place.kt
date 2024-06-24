package com.jdccmobile.domain.model

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
