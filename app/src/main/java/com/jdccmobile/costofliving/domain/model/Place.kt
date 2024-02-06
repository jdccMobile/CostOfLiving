package com.jdccmobile.costofliving.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Place(
    val cityName: String? = null,
    val countryName: String
) : Parcelable