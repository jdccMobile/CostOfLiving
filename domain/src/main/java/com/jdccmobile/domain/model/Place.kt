package com.jdccmobile.domain.model

data class City(
    val cityId: Int,
    val countryName: String,
    val cityName: String,
    val isFavorite: Boolean = false,
    val placeType: PlaceType = PlaceType.City,
    val citiesInCountry: Int? = null,
)

data class Country(
    val countryId: String,
    val countryName: String,
    val isFavorite: Boolean = false,
    val placeType: PlaceType = PlaceType.Country,
)

enum class PlaceType { City, Country }
