package com.jdccmobile.costofliving.ui.models

import android.os.Parcelable
import com.jdccmobile.domain.model.Place
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class PlaceUi : Parcelable {
    abstract val countryName: String
    abstract val cityName: String
    abstract val isFavorite: Boolean

    data class City(
        override val countryName: String,
        override val cityName: String,
        override val isFavorite: Boolean = false,
        val cityId: Int,
    ) : PlaceUi()

    data class Country(
        override val countryName: String,
        override val cityName: String = "",
        override val isFavorite: Boolean = false,
        val countryId: String
    ) : PlaceUi()
}

fun PlaceUi.toDomain() = when (this) {
    is PlaceUi.City -> {
        Place.City(
            cityId = cityId,
            cityName = cityName,
            countryName = countryName,
            isFavorite = isFavorite,
        )
    }

    is PlaceUi.Country -> {
        Place.Country(
            countryId = countryId,
            countryName = countryName,
            isFavorite = isFavorite,
        )
    }
}

fun List<Place>.toUi() = map { place ->
    when (place) {
        is Place.City -> {
            PlaceUi.City(
                cityId = place.cityId,
                cityName = place.cityName,
                countryName = place.countryName,
                isFavorite = place.isFavorite,
            )
        }
        is Place.Country -> {
            PlaceUi.Country(
                countryId = place.countryId,
                countryName = place.countryName,
                isFavorite = place.isFavorite,
            )
        }
    }
}

fun List<Place.City>.toCityUi() = map {
    PlaceUi.City(
        cityId = it.cityId,
        cityName = it.cityName,
        countryName = it.countryName,
        isFavorite = it.isFavorite,
    )
}

fun PlaceUi.City.toCityDomain() =
    Place.City(
        cityId = cityId,
        cityName = cityName,
        countryName = countryName,
        isFavorite = isFavorite,
    )
