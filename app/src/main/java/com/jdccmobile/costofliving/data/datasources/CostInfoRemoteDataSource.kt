package com.jdccmobile.costofliving.data.datasources

import com.jdccmobile.costofliving.App
import com.jdccmobile.costofliving.R
import com.jdccmobile.costofliving.data.remote.RetrofitServiceFactory

class CostInfoRemoteDataSource(application: App) {
    private val service = RetrofitServiceFactory.makeRetrofitService()
    private val apiKey = application.getString(R.string.api_key)

    suspend fun getCitiesList() = service.getCities(apiKey)

    suspend fun getCityCost(cityName: String, countryName: String) = service.getCityCost(
        apiKey,
        cityName,
        countryName,
    )

    suspend fun getCountryCost(countryName: String) = service.getCountryCost(apiKey, countryName)
}
