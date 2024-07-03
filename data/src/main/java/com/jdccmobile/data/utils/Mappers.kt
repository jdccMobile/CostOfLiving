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

fun List<FavoritePlaceDb>.toDomain(): List<Place> = map { favoritePlace ->
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
