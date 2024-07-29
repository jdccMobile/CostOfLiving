package com.jdccmobile.domain.model

data class City(
    val cityId: Int,
    val countryName: String,
    val cityName: String,
    val isFavorite: Boolean = false,
    val citiesInCountry: Int? = null,
)
