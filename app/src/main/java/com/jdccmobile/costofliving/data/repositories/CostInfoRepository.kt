package com.jdccmobile.costofliving.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.jdccmobile.costofliving.App
import com.jdccmobile.costofliving.data.datasources.CostInfoLocalDataSource
import com.jdccmobile.costofliving.data.datasources.CostInfoRemoteDataSource
import com.jdccmobile.costofliving.data.datasources.PreferencesDataSource
import com.jdccmobile.costofliving.domain.models.ItemPrice
import com.jdccmobile.costofliving.domain.models.Place

class CostInfoRepository(application: App, dataStore: DataStore<Preferences>) {
    private val preferencesDataSource = PreferencesDataSource(dataStore)
    private val localDataSource = CostInfoLocalDataSource()
    private val remoteDataSource = CostInfoRemoteDataSource(application)

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
