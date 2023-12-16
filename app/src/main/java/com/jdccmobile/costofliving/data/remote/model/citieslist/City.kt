package com.jdccmobile.costofliving.data.remote.model.citieslist

import com.google.gson.annotations.SerializedName

data class City(
    @SerializedName("city_name") val cityName: String,
    @SerializedName("country_name") val countryName: String,
)