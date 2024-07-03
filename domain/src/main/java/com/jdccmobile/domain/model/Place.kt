package com.jdccmobile.domain.model

sealed class Place {
    abstract val countryName: String
    abstract val isFavorite: Boolean?

    data class City(
        override val countryName: String,
        override val isFavorite: Boolean? = null,
        val cityName: String,
    ) : Place()

    data class Country(
        override val countryName: String,
        override val isFavorite: Boolean? = null,
    ) : Place()
}
