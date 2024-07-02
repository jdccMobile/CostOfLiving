package com.jdccmobile.data.database.datasources

import com.jdccmobile.data.database.FavoriteCityDao
import com.jdccmobile.data.database.FavoriteCityDb
import com.jdccmobile.domain.model.Place
import kotlinx.coroutines.flow.map

class PlaceLocalDataSource(private val favoriteCityDao: FavoriteCityDao) {
    suspend fun insertFavoriteCity(city: Place.City) =
        favoriteCityDao.insertFavoriteCity(city.toDb())

//    suspend fun getFavoriteCitiesList(): Flow<List<Place.City>> =
//        favoriteCityDao.getFavoriteCitiesList().toDomain()

//    suspend fun deleteFavoriteCity(city: Place.City) =
//        favoriteCityDao.deleteFavoriteCity(null)
}

// TODO jdc donde usar el mapper en el repositorio o en el data source
fun Place.City.toDb() = FavoriteCityDb(
    id = null,
    cityName = cityName,
    countryName = countryName,
)

fun List<FavoriteCityDb>.toDomain(): List<Place.City> = map {
    Place.City(
        cityName = it.cityName,
        countryName = it.countryName,
    )
}
