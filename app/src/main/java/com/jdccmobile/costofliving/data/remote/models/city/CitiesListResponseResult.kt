package com.jdccmobile.costofliving.data.remote.models.city

data class CitiesListResponseResult(
    val cities: List<CityResponse>,
    val error: Any,
)
