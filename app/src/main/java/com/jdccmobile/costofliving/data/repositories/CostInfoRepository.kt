package com.jdccmobile.costofliving.data.repositories

import com.jdccmobile.costofliving.data.local.datasources.CostInfoLocalDataSource
import com.jdccmobile.costofliving.data.local.datasources.PreferencesDataSource
import com.jdccmobile.costofliving.data.remote.datasources.CostInfoRemoteDataSource
import com.jdccmobile.costofliving.domain.models.ItemPrice
import com.jdccmobile.costofliving.domain.models.Place

class CostInfoRepository(
    private val preferencesDataSource: PreferencesDataSource, // todo utilizar interfaces para lograr inversion de dependencias
    private val localDataSource: CostInfoLocalDataSource,
    private val remoteDataSource: CostInfoRemoteDataSource,
) {
    // Preferences requests
    suspend fun requestUserCountryPrefs(): String = preferencesDataSource.getUserCountryPrefs()

    suspend fun saveUserCountryPrefs(countryName: String): Unit =
        preferencesDataSource.putUserCountryPrefs(countryName)

    // Remote requests
    suspend fun requestCitiesList(): List<Place.City> = remoteDataSource.getCitiesList()

    suspend fun requestCityCost(cityName: String, countryName: String): List<ItemPrice> =
        remoteDataSource.getCityCost(cityName, countryName)

    suspend fun requestCountryCost(countryName: String): List<ItemPrice> =
        remoteDataSource.getCountryCost(countryName)
}
