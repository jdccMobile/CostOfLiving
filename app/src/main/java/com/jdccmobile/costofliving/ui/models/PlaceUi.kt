package com.jdccmobile.costofliving.ui.models

import android.os.Parcelable
import com.jdccmobile.domain.model.Place
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class PlaceUi : Parcelable {
    abstract val countryName: String
    abstract val cityName: String
    abstract val isFavorite: Boolean?

    data class City(
        override val countryName: String,
        override val cityName: String,
        override val isFavorite: Boolean? = null,
    ) : PlaceUi()

    data class Country(
        override val countryName: String,
        override val cityName: String = "",
        override val isFavorite: Boolean? = null,
    ) : PlaceUi()
}

fun PlaceUi.toDomain() = when (this) {
    is PlaceUi.City -> {
        Place.City(
            cityName = cityName,
            countryName = countryName,
            isFavorite = isFavorite,
        )
    }

    is PlaceUi.Country -> {
        Place.Country(
            countryName = countryName,
            isFavorite = isFavorite,
        )
    }
}

fun List<Place>.toUi() = map { place ->
    when (place) {
        is Place.City -> {
            PlaceUi.City(
                cityName = place.cityName,
                countryName = place.countryName,
                isFavorite = place.isFavorite,
            )
        }
        is Place.Country -> {
            PlaceUi.Country(
                countryName = place.countryName,
                isFavorite = place.isFavorite,
            )
        }
    }
}

fun List<Place.City>.toCityUi() = map {
    PlaceUi.City(
        cityName = it.cityName,
        countryName = it.countryName,
        isFavorite = it.isFavorite,
    )
}

fun PlaceUi.City.toCityDomain() =
    Place.City(
        cityName = cityName,
        countryName = countryName,
        isFavorite = isFavorite,
    )
