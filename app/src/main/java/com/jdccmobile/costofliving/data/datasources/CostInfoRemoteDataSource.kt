package com.jdccmobile.costofliving.data.datasources

import com.jdccmobile.costofliving.App
import com.jdccmobile.costofliving.R
import com.jdccmobile.costofliving.data.remote.RetrofitServiceFactory
import com.jdccmobile.costofliving.data.remote.models.city.CitiesListResponseResult
import com.jdccmobile.costofliving.data.remote.models.cost.PriceResponse
import com.jdccmobile.costofliving.domain.models.ItemPrice
import com.jdccmobile.costofliving.domain.models.Place

class CostInfoRemoteDataSource(application: App) {
    private val service = RetrofitServiceFactory.makeRetrofitService()
    private val apiKey = application.getString(R.string.api_key)

    suspend fun getCitiesList(): List<Place.City> = service.getCities(apiKey).toDomain()

    suspend fun getCityCost(cityName: String, countryName: String): List<ItemPrice> =
        service.getCityCost(apiKey, cityName, countryName).prices.toDomain()

    suspend fun getCountryCost(countryName: String): List<ItemPrice> =
        service.getCountryCost(apiKey, countryName).prices.toDomain()
}

private fun CitiesListResponseResult.toDomain(): List<Place.City> = this.cities.map { city ->
    Place.City(
        cityName = city.cityName,
        countryName = city.countryName,
    )
}

private fun List<PriceResponse>.toDomain(): List<ItemPrice> = this.map { item ->
    ItemPrice(
        name = item.itemName,
        cost = item.avg,
    )
}
