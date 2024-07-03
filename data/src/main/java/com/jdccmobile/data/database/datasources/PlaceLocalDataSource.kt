package com.jdccmobile.data.database.datasources

import com.jdccmobile.data.database.FavoritePlaceDao
import com.jdccmobile.data.utils.toCityDomain
import com.jdccmobile.data.utils.toCountryDomain
import com.jdccmobile.data.utils.toDb
import com.jdccmobile.data.utils.toPlaceDomain
import com.jdccmobile.domain.model.Place

class PlaceLocalDataSource(private val favoritePlaceDao: FavoritePlaceDao) {
    suspend fun getFavoritePlacesList(): List<Place> =
        favoritePlaceDao.getFavoritePlacesList().toPlaceDomain()


    suspend fun insertFavoriteCity(city: Place): Unit =
        favoritePlaceDao.insertFavoriteCity(city.toDb())

    suspend fun getFavoriteCitiesList(): List<Place.City> =
        favoritePlaceDao.getFavoriteCitiesList().toCityDomain()

    suspend fun deleteFavoriteCity(cityName: String, countryName: String): Unit =
        favoritePlaceDao.deleteFavoriteCity(cityName, countryName)

    suspend fun checkIsFavoriteCity(cityName: String, countryName: String): Boolean =
        favoritePlaceDao.countFavoriteCity(cityName, countryName) > 0


    suspend fun insertFavoriteCountry(country: Place.Country): Unit =
        favoritePlaceDao.insertFavoriteCountry(country.toDb())

    suspend fun getFavoriteCountriesList(): List<Place.Country> =
        favoritePlaceDao.getFavoriteCountriesList().toCountryDomain()

    suspend fun deleteFavoriteCountry(countryName: String): Unit =
        favoritePlaceDao.deleteFavoriteCountry(countryName)

    suspend fun checkIsFavoriteCountry(countryName: String): Boolean =
        favoritePlaceDao.countFavoriteCountry(countryName) > 0
}
