package com.jdccmobile.costofliving.data.remote.model

import com.google.gson.annotations.SerializedName

data class City(
    @SerializedName("city_id") val cityId: Int,
    @SerializedName("city_name") val cityName: String,
    @SerializedName("country_name") val countryName: String,
)