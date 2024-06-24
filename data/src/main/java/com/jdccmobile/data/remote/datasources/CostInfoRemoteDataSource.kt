package com.jdccmobile.data.remote.datasources

import com.jdccmobile.costofliving.App
import com.jdccmobile.costofliving.R
import com.jdccmobile.costofliving.data.remote.RetrofitServiceFactory
import com.jdccmobile.costofliving.data.remote.models.city.CitiesListResponseResult
import com.jdccmobile.costofliving.data.remote.models.cost.PriceResponse
import com.jdccmobile.domain.model.Place

class CostInfoRemoteDataSource(application: App) {
    private val service = RetrofitServiceFactory.makeRetrofitService()
    private val apiKey = application.getString(R.string.api_key)

    suspend fun getCitiesList(): List<com.jdccmobile.domain.model.Place.City> = service.getCities(
        apiKey,
    ).toDomain()

    suspend fun getCityCost(cityName: String, countryName: String): List<com.jdccmobile.domain.model.ItemPrice> =
        service.getCityCost(apiKey, cityName, countryName).prices.toDomain()

    suspend fun getCountryCost(countryName: String): List<com.jdccmobile.domain.model.ItemPrice> =
        service.getCountryCost(apiKey, countryName).prices.toDomain()
}

private fun CitiesListResponseResult.toDomain(): List<com.jdccmobile.domain.model.Place.City> = this.cities.map { city ->
    com.jdccmobile.domain.models.Place.City(
        cityName = city.cityName,
        countryName = city.countryName,
    )
}

private fun List<PriceResponse>.toDomain(): List<com.jdccmobile.domain.model.ItemPrice> = this.map { item ->
    com.jdccmobile.domain.model.ItemPrice(
        name = item.itemName,
        cost = item.avg,
    )
}
