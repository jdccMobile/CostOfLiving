package com.jdccmobile.data.remote.datasources

import com.jdccmobile.data.remote.RetrofitService
import com.jdccmobile.data.remote.models.city.CitiesListResponseResult
import com.jdccmobile.data.remote.models.cost.PriceResponse
import com.jdccmobile.domain.model.ItemPrice
import com.jdccmobile.domain.model.Place

class PlaceRemoteDataSource(private val apiKey: String, private val service: RetrofitService) {
    suspend fun getCitiesList(): List<Place.City> = service.getCities(
        apiKey,
    ).toDomain()

    suspend fun getCityCost(cityName: String, countryName: String): List<ItemPrice> =
        service.getCityCost(apiKey, cityName, countryName).prices.toDomain()

    suspend fun getCountryCost(countryName: String): List<ItemPrice> =
        service.getCountryCost(apiKey, countryName).prices.toDomain()
}

private fun CitiesListResponseResult.toDomain(): List<Place.City> = this.cities.map { city ->
    Place.City(
        cityId = city.cityId,
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
