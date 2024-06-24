package com.jdccmobile.data.remote.models.city

data class CitiesListResponseResult(
    val cities: List<CityResponse>,
    val error: Any,
)
