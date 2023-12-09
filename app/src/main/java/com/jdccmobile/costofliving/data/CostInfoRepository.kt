package com.jdccmobile.costofliving.data

import com.jdccmobile.costofliving.App
import com.jdccmobile.costofliving.data.datasources.CostInfoLocalDataSource
import com.jdccmobile.costofliving.data.datasources.CostInfoRemoteDataSource

class CostInfoRepository(application: App) {

    private val localDataSource = CostInfoLocalDataSource()
    private val remoteDataSource = CostInfoRemoteDataSource(application)

    suspend fun requestCitiesList() = remoteDataSource.getCitiesList()

    suspend fun requestCityCost(cityName: String, countryName: String) = remoteDataSource.getCityCost(cityName, countryName)
}
