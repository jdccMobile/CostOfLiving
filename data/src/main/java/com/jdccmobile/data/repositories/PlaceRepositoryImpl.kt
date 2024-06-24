package com.jdccmobile.data.repositories

import com.jdccmobile.data.local.datasources.CostInfoLocalDataSource
import com.jdccmobile.data.remote.datasources.CostInfoRemoteDataSource
import com.jdccmobile.domain.model.ItemPrice
import com.jdccmobile.domain.model.Place
import com.jdccmobile.domain.repository.PlaceRepository

class PlaceRepositoryImpl(
    private val localDataSource: CostInfoLocalDataSource,
    private val remoteDataSource: CostInfoRemoteDataSource,
) : PlaceRepository {
    override suspend fun getCitiesListRemote(): List<Place.City> = remoteDataSource.getCitiesList()

    override suspend fun getCityCostRemote(cityName: String, countryName: String): List<ItemPrice> =
        remoteDataSource.getCityCost(cityName, countryName)

    override suspend fun getCountryCostRemote(countryName: String): List<ItemPrice> =
        remoteDataSource.getCountryCost(countryName)
}
