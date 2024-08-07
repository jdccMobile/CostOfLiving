package com.jdccmobile.data.remote.models.city

import com.google.gson.annotations.SerializedName

data class CityResponse(
    @SerializedName("city_id") val cityId: Int,
    @SerializedName("city_name") val cityName: String,
    @SerializedName("country_name") val countryName: String,
)
