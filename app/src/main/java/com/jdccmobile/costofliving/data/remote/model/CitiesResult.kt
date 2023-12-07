package com.jdccmobile.costofliving.data.remote.model

data class CitiesResult(
    val cities: List<CityApi>,
    val error: Any
)