package com.jdccmobile.costofliving.ui.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CityUi(
    val countryName: String,
    val cityName: String,
    val isFavorite: Boolean = false,
    val cityId: Int,
) : Parcelable
