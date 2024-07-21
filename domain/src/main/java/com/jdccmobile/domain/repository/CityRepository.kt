package com.jdccmobile.domain.repository

import arrow.core.Either
import com.jdccmobile.domain.model.City
import com.jdccmobile.domain.model.CityCost
import com.jdccmobile.domain.model.ErrorType

interface CityRepository {
    // Local
    suspend fun insertCity(city: City): Either<Throwable, Unit>

    suspend fun insertCitiesFromUserCountry(cities: List<City>)

    suspend fun getCitiesFromUserCountry(countryName: String): Either<Throwable, List<City>>

    suspend fun getCity(cityId: Int): Either<Throwable, City>

    suspend fun getFavoriteCities(): Either<Throwable, List<City>>

    suspend fun updateCity(city: City): Either<Throwable, Unit>

    suspend fun getCityCostLocal(cityId: Int): Either<Throwable, CityCost?>

    suspend fun insertCityCostLocal(cityCost: CityCost): Either<Throwable, Unit>

    // Remote
    suspend fun getCitiesListRemote(): Either<Throwable, List<City>>

    suspend fun getCityCostRemote(
        cityName: String,
        countryName: String,
    ): Either<Throwable, CityCost>
}
