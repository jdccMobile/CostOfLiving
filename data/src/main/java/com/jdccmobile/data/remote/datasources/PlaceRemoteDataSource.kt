package com.jdccmobile.data.remote.datasources

import com.jdccmobile.data.remote.RetrofitService
import com.jdccmobile.data.remote.models.city.CitiesListResponseResult
import com.jdccmobile.data.remote.models.cost.PriceResponse
import com.jdccmobile.domain.model.City
import com.jdccmobile.domain.model.CityCost
import com.jdccmobile.domain.model.ItemPrice

class PlaceRemoteDataSource(private val apiKey: String, private val service: RetrofitService) {
    suspend fun getCitiesList(): List<City> = service.getCities(
        apiKey,
    ).toDomain()

    suspend fun getCityCost(cityName: String, countryName: String): CityCost =
        service.getCityCost(apiKey, cityName, countryName).prices.toDomain()

//    suspend fun getCountryCost(countryName: String): List<ItemPrice> =
//        service.getCountryCost(apiKey, countryName).prices.toDomain()
}

private fun CitiesListResponseResult.toDomain(): List<City> = this.cities.map { city ->
    City(
        cityId = city.cityId,
        cityName = city.cityName,
        countryName = city.countryName,
    )
}

fun List<PriceResponse>.toDomain(): CityCost {
    val defaultPrice = 0.0
    val pricesMap = mutableMapOf<Int, Double>()
    val cityId = this.map { it.cityId }.first()
    this.forEach { item ->
        pricesMap[item.goodId] = item.avg
    }

    return CityCost(
        cityId = cityId,
        housePrice = pricesMap[1] ?: defaultPrice,
        gasolinePrice = pricesMap[45] ?: defaultPrice,
        dressPrice = pricesMap[64] ?: defaultPrice,
        gymPrice = pricesMap[43] ?: defaultPrice,
        cocaColaPrice = pricesMap[33] ?: defaultPrice,
        mcMealPrice = pricesMap[36] ?: defaultPrice,
    )
}
