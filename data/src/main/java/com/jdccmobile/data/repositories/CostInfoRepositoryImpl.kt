package com.jdccmobile.data.repositories

import com.jdccmobile.data.local.datasources.CostInfoLocalDataSource
import com.jdccmobile.data.local.datasources.PreferencesDataSource
import com.jdccmobile.data.remote.datasources.CostInfoRemoteDataSource
import com.jdccmobile.domain.model.ItemPrice
import com.jdccmobile.domain.model.Place
import com.jdccmobile.domain.repository.CostInfoRepository

class CostInfoRepositoryImpl(
    private val preferencesDataSource: PreferencesDataSource,
    private val localDataSource: CostInfoLocalDataSource,
    private val remoteDataSource: CostInfoRemoteDataSource,
) : CostInfoRepository {
    override suspend fun requestUserCountryPrefs(): String = preferencesDataSource.getUserCountryPrefs()

    override suspend fun saveUserCountryPrefs(countryName: String): Unit =
        preferencesDataSource.putUserCountryPrefs(countryName)

    override suspend fun requestCitiesListRemote(): List<Place.City> = remoteDataSource.getCitiesList()

    override suspend fun requestCityCostRemote(cityName: String, countryName: String): List<ItemPrice> =
        remoteDataSource.getCityCost(cityName, countryName)

    override suspend fun requestCountryCostRemote(countryName: String): List<ItemPrice> =
        remoteDataSource.getCountryCost(countryName)
}
