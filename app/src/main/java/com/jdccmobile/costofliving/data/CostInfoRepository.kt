package com.jdccmobile.costofliving.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.jdccmobile.costofliving.App
import com.jdccmobile.costofliving.data.datasources.CostInfoLocalDataSource
import com.jdccmobile.costofliving.data.datasources.CostInfoRemoteDataSource
import com.jdccmobile.costofliving.data.datasources.PreferencesDataSource

class CostInfoRepository(application: App, dataStore: DataStore<Preferences>) {
    private val preferencesDataSource = PreferencesDataSource(dataStore)
    private val localDataSource = CostInfoLocalDataSource()
    private val remoteDataSource = CostInfoRemoteDataSource(application)

    // Preferences requests
    suspend fun requestUserCountryPrefs() = preferencesDataSource.getUserCountryPrefs()

    suspend fun saveUserCountryPrefs(countryName: String) = preferencesDataSource.putUserCountryPrefs(
        countryName,
    )

    // Remote requests
    suspend fun requestCitiesList() = remoteDataSource.getCitiesList()

    suspend fun requestCityCost(cityName: String, countryName: String) = remoteDataSource.getCityCost(
        cityName,
        countryName,
    )

    suspend fun requestCountryCost(countryName: String) = remoteDataSource.getCountryCost(
        countryName,
    )
}
