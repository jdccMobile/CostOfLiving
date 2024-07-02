package com.jdccmobile.data.database.datasources

import com.jdccmobile.data.database.FavoriteCityDao
import com.jdccmobile.data.database.FavoriteCityDb
import com.jdccmobile.domain.model.Place
import kotlinx.coroutines.flow.map

class PlaceLocalDataSource(private val favoriteCityDao: FavoriteCityDao) {
    suspend fun insertFavoriteCity(city: Place.City): Unit =
        favoriteCityDao.insertFavoriteCity(city.toDb())

    suspend fun getFavoriteCitiesList(): List<Place.City> =
        favoriteCityDao.getFavoriteCitiesList().toDomain()

    suspend fun deleteFavoriteCity(cityName: String, countryName: String): Unit =
        favoriteCityDao.deleteFavoriteCity(cityName, countryName)

    suspend fun checkIsFavoriteCity(cityName: String, countryName: String): Boolean =
        favoriteCityDao.countFavoriteCity(cityName, countryName) > 0
}

// TODO jdc donde usar el mapper en el repositorio o en el data source
fun Place.City.toDb() = FavoriteCityDb(
    id = null,
    cityName = cityName,
    countryName = countryName,
    isFavorite = true
)

fun List<FavoriteCityDb>.toDomain(): List<Place.City> = map {
    Place.City(
        cityName = it.cityName,
        countryName = it.countryName,
        isFavorite = it.isFavorite,
    )
}
