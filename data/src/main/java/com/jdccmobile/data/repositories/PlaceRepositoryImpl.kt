package com.jdccmobile.data.repositories

import com.jdccmobile.data.database.datasources.PlaceLocalDataSource
import com.jdccmobile.data.remote.datasources.PlaceRemoteDataSource
import com.jdccmobile.domain.model.ItemPrice
import com.jdccmobile.domain.model.Place
import com.jdccmobile.domain.repository.PlaceRepository

class PlaceRepositoryImpl(
    private val localDataSource: PlaceLocalDataSource,
    private val remoteDataSource: PlaceRemoteDataSource,
) : PlaceRepository {
    // Database
    override suspend fun insertFavoriteCity(city: Place.City) =
        localDataSource.insertFavoriteCity(city)

    override suspend fun getFavoriteCitiesList(): List<Place.City> =
        localDataSource.getFavoriteCitiesList()

    override suspend fun deleteFavoriteCity(city: Place.City) =
        localDataSource.deleteFavoriteCity(city.cityName, city.countryName)


    // Remote
    override suspend fun getCitiesListRemote(): List<Place.City> = remoteDataSource.getCitiesList()

    override suspend fun getCityCostRemote(cityName: String, countryName: String): List<ItemPrice> =
        remoteDataSource.getCityCost(cityName, countryName)

    override suspend fun getCountryCostRemote(countryName: String): List<ItemPrice> =
        remoteDataSource.getCountryCost(countryName)
}
