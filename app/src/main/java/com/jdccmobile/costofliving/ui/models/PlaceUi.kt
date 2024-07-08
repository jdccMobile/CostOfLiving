package com.jdccmobile.costofliving.ui.models

import android.os.Parcelable
import com.jdccmobile.domain.model.PlaceType
import kotlinx.parcelize.Parcelize

@Parcelize
data class CityUi(
    val countryName: String,
    val cityName: String,
    val isFavorite: Boolean = false,
    val cityId: Int,
    val placeType: PlaceType = PlaceType.City,
) : Parcelable

@Parcelize
data class CountryUi(
    val countryName: String,
    val cityName: String = "",
    val isFavorite: Boolean = false,
    val countryId: String,
    val placeType: PlaceType = PlaceType.Country,
) : Parcelable
