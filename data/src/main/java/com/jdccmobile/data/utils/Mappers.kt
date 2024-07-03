package com.jdccmobile.data.utils

import com.jdccmobile.data.database.FavoritePlaceDb
import com.jdccmobile.domain.model.Place

fun Place.toDb() = when (this) {
    is Place.City -> {
        FavoritePlaceDb(
            cityName = cityName,
            countryName = countryName,
            isFavorite = true,
        )
    }

    is Place.Country -> {
        FavoritePlaceDb(
            cityName = null,
            countryName = countryName,
            isFavorite = true,
        )
    }
}

fun List<FavoritePlaceDb>.toPlaceDomain(): List<Place> = map { favoritePlace ->
    if (favoritePlace.cityName != null) {
        Place.City(
            cityName = favoritePlace.cityName,
            countryName = favoritePlace.countryName,
            isFavorite = favoritePlace.isFavorite,
        )
    } else {
        Place.Country(
            countryName = favoritePlace.countryName,
            isFavorite = favoritePlace.isFavorite,
        )
    }
}

fun List<FavoritePlaceDb>.toCityDomain(): List<Place.City> = map {
    Place.City(
        cityName = it.cityName ?: "",
        countryName = it.countryName,
        isFavorite = it.isFavorite,
    )
}

fun List<FavoritePlaceDb>.toCountryDomain(): List<Place.Country> = map {
    Place.Country(
        countryName = it.countryName,
        isFavorite = it.isFavorite,
    )
}