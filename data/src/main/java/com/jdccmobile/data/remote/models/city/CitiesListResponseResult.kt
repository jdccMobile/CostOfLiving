package com.jdccmobile.data.remote.models.city

data class CitiesListResponseResult(
    val cities: List<com.jdccmobile.data.remote.models.city.CityResponse>,
    val error: Any,
)
