package com.jdccmobile.domain.model

sealed class Place {
    abstract val countryName: String
    abstract val cityName: String?
    abstract val isFavorite: Boolean
    abstract val placeType: PlaceType

    data class City(
        val cityId: Int,
        override val countryName: String,
        override val cityName: String,
        override val isFavorite: Boolean = false,
        override val placeType: PlaceType = PlaceType.City,
    ) : Place()

    data class Country(
        val countryId: String,
        override val countryName: String,
        override val cityName: String? = null,
        override val isFavorite: Boolean = false,
        override val placeType: PlaceType = PlaceType.Country,
    ) : Place()
}

enum class PlaceType { City, Country }