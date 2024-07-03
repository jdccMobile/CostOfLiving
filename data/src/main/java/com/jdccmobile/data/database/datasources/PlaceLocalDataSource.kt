package com.jdccmobile.data.database.datasources

import com.jdccmobile.data.database.FavoritePlaceDao
import com.jdccmobile.data.utils.toDb
import com.jdccmobile.data.utils.toDomain
import com.jdccmobile.domain.model.Place

class PlaceLocalDataSource(private val favoritePlaceDao: FavoritePlaceDao) {
    suspend fun insertFavoritePlace(place: Place): Unit =
        favoritePlaceDao.insertFavoritePlace(place.toDb())

    suspend fun getFavoritePlacesList(): List<Place> =
        favoritePlaceDao.getFavoritePlacesList().toDomain()

    suspend fun deleteFavoritePlace(place: Place): Unit =
        favoritePlaceDao.deleteFavoritePlace(
            cityName = place.cityName,
            countryName = place.countryName,
        )

    suspend fun checkIsFavoritePlace(place: Place): Boolean =
        favoritePlaceDao.countFavoritePlace(
            cityName = place.cityName,
            countryName = place.countryName,
        ) > 0
}
