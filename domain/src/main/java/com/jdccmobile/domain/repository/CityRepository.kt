package com.jdccmobile.domain.repository

import arrow.core.Either
import com.jdccmobile.domain.model.City
import com.jdccmobile.domain.model.CityCost

interface CityRepository {
    // Local
    suspend fun insertCity(city: City)

    suspend fun insertCitiesFromUserCountry(cities: List<City>)

    suspend fun getCitiesFromUserCountry(countryName: String):  Either<Throwable, List<City>>

    suspend fun getCity(cityId: Int): City

    suspend fun getFavoriteCities(): List<City>

    suspend fun updateCity(city: City)

    suspend fun getCityCostLocal(cityId: Int): CityCost?

    suspend fun insertCityCostLocal(cityCost: CityCost)

    // Remote
    suspend fun getCitiesListRemote(): Either<Throwable, List<City>>

    suspend fun getCityCostRemote(cityName: String, countryName: String): CityCost
}
