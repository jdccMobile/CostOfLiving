package com.jdccmobile.costofliving.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize // TODO in domain we dont should hace a parcelabe
sealed class Place : Parcelable {
    abstract val countryName: String

    data class City(
        override val countryName: String,
        val cityName: String,
    ) : Place()

    data class Country(
        override val countryName: String,
    ) : Place()
}
