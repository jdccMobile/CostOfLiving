package com.jdccmobile.data.remote.datasources

import arrow.core.Either
import arrow.core.Either.Companion.catch
import com.jdccmobile.data.remote.RetrofitService
import com.jdccmobile.data.remote.models.city.CitiesListResponseResult
import com.jdccmobile.data.remote.models.cost.CityCostResponseResult
import com.jdccmobile.domain.model.City
import com.jdccmobile.domain.model.CityCost

class CityRemoteDataSource(private val apiKey: String, private val service: RetrofitService) {
    suspend fun getCitiesList(): Either<Throwable, List<City>> =
        catch { service.getCities(apiKey).toDomain() }

    suspend fun getCityCost(cityName: String, countryName: String): Either<Throwable, CityCost> =
        catch { service.getCityCost(apiKey, cityName, countryName).toDomain() }
}

private fun CitiesListResponseResult.toDomain(): List<City> = this.cities.map { city ->
    City(
        cityId = city.cityId,
        cityName = city.cityName,
        countryName = city.countryName,
    )
}

fun CityCostResponseResult.toDomain(): CityCost {
    val defaultPrice = 0.0
    val pricesMap = mutableMapOf<Int, Double>()
    val cityId = this.cityId
    this.prices.forEach { item ->
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
