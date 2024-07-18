package com.jdccmobile.data.repositories

import arrow.core.Either
import com.jdccmobile.data.database.datasources.CityLocalDataSource
import com.jdccmobile.data.database.datasources.CostLifeLocalDataSource
import com.jdccmobile.data.remote.datasources.PlaceRemoteDataSource
import com.jdccmobile.domain.model.City
import com.jdccmobile.domain.model.CityCost
import com.jdccmobile.domain.repository.CityRepository

class CityRepositoryImpl(
    private val cityLocalDataSource: CityLocalDataSource,
    private val costLifeLocalDataSource: CostLifeLocalDataSource,
    private val remoteDataSource: PlaceRemoteDataSource,
) : CityRepository {
    // Local
    // City
    override suspend fun insertCity(city: City) {
        cityLocalDataSource.insertCity(city)
    }

    override suspend fun insertCitiesFromUserCountry(cities: List<City>) {
        cityLocalDataSource.insertCitiesFromUserCountry(cities)
    }

    override suspend fun getCitiesFromUserCountry(countryName: String): List<City> =
        cityLocalDataSource.getCitiesFromUserCountry(countryName)

    override suspend fun getCity(cityId: Int): City {
        return cityLocalDataSource.getCity(cityId)
    }

    override suspend fun getFavoriteCities(): List<City> =
        cityLocalDataSource.getFavoriteCities()

    override suspend fun updateCity(city: City) {
        cityLocalDataSource.updateCity(city)
    }

    override suspend fun getCityCostLocal(cityId: Int): CityCost? =
        costLifeLocalDataSource.getCityCostLocal(cityId)

    override suspend fun insertCityCostLocal(cityCost: CityCost) {
        costLifeLocalDataSource.insertCityCost(cityCost)
    }

    // Remote
    override suspend fun getCitiesListRemote(): Either<Throwable, List<City>> =
        remoteDataSource.getCitiesList()

    override suspend fun getCityCostRemote(cityName: String, countryName: String): CityCost =
        remoteDataSource.getCityCost(cityName, countryName)
}
