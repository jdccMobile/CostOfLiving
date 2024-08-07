package com.jdccmobile.data.repositories

import arrow.core.Either
import com.jdccmobile.data.database.datasources.CityLocalDataSource
import com.jdccmobile.data.database.datasources.CostLifeLocalDataSource
import com.jdccmobile.data.remote.datasources.CityRemoteDataSource
import com.jdccmobile.domain.model.City
import com.jdccmobile.domain.model.CityCost
import com.jdccmobile.domain.repository.CityRepository

@Suppress("TooManyFunctions")
class CityRepositoryImpl(
    private val cityLocalDataSource: CityLocalDataSource,
    private val costLifeLocalDataSource: CostLifeLocalDataSource,
    private val remoteDataSource: CityRemoteDataSource,
) : CityRepository {
    // Local
    // City
    override suspend fun insertCity(city: City): Either<Throwable, Unit> =
        cityLocalDataSource.insertCity(city)

    override suspend fun insertCitiesFromUserCountry(cities: List<City>): Either<Throwable, Unit> =
        cityLocalDataSource.insertCitiesFromUserCountry(cities)

    override suspend fun getCities(): Either<Throwable, List<City>> =
        cityLocalDataSource.getCities()

    override suspend fun getCitiesFromUserCountry(
        countryName: String,
    ): Either<Throwable, List<City>> = cityLocalDataSource.getCitiesFromUserCountry(countryName)

    override suspend fun getCity(cityId: Int): Either<Throwable, City> {
        return cityLocalDataSource.getCity(cityId)
    }

    override suspend fun getFavoriteCities(): Either<Throwable, List<City>> =
        cityLocalDataSource.getFavoriteCities()

    override suspend fun updateCity(city: City): Either<Throwable, Unit> =
        cityLocalDataSource.updateCity(city)

    override suspend fun getCityCostLocal(cityId: Int): Either<Throwable, CityCost?> =
        costLifeLocalDataSource.getCityCostLocal(cityId)

    override suspend fun insertCityCostLocal(cityCost: CityCost): Either<Throwable, Unit> =
        costLifeLocalDataSource.insertCityCost(cityCost)

    // Remote
    override suspend fun getCitiesListRemote(): Either<Throwable, List<City>> =
        remoteDataSource.getCitiesList()

    override suspend fun getCityCostRemote(
        cityName: String,
        countryName: String,
    ): Either<Throwable, CityCost> =
        remoteDataSource.getCityCost(cityName, countryName)
}
