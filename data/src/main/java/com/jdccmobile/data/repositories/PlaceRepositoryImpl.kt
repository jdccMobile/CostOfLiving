package com.jdccmobile.data.repositories

import com.jdccmobile.data.database.datasources.PlaceLocalDataSource
import com.jdccmobile.data.remote.datasources.PlaceRemoteDataSource
import com.jdccmobile.domain.model.ItemPrice
import com.jdccmobile.domain.model.Place
import com.jdccmobile.domain.repository.PlaceRepository

class PlaceRepositoryImpl(
    private val localDataSource: com.jdccmobile.data.database.datasources.PlaceLocalDataSource,
    private val remoteDataSource: PlaceRemoteDataSource,
) : PlaceRepository {
    // Local
    override suspend fun getFavoritePlacesList(): List<Place> =
        localDataSource.getFavoritePlacesList()

    override suspend fun insertFavoritePlace(place: Place): Unit =
        localDataSource.insertFavoritePlace(place)

    override suspend fun deleteFavoritePlace(place: Place) =
        localDataSource.deleteFavoritePlace(place)

    override suspend fun checkIsFavoritePlace(place: Place): Boolean =
        localDataSource.checkIsFavoritePlace(place)

    // Remote
    override suspend fun getCitiesListRemote(): List<Place.City> = remoteDataSource.getCitiesList()

    // Todo asd unificar los dos ultimos
    override suspend fun getCityCostRemote(cityName: String, countryName: String): List<ItemPrice> =
        remoteDataSource.getCityCost(cityName, countryName)

    override suspend fun getCountryCostRemote(countryName: String): List<ItemPrice> =
        remoteDataSource.getCountryCost(countryName)
}
