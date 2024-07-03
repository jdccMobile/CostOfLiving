package com.jdccmobile.domain.model

sealed class Place {
    abstract val countryName: String
    abstract val cityName: String?
    abstract val isFavorite: Boolean?

    data class City(
        override val countryName: String,
        override val cityName: String,
        override val isFavorite: Boolean? = null,
    ) : Place()

    data class Country(
        override val countryName: String,
        override val cityName: String? = null,
        override val isFavorite: Boolean? = null,
    ) : Place()
}
