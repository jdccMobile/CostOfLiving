package com.jdccmobile.data.repositories

import com.jdccmobile.data.database.datasources.CityDbDataSource
import com.jdccmobile.data.database.datasources.CostLifeDbDataSource
import com.jdccmobile.data.remote.datasources.PlaceRemoteDataSource
import com.jdccmobile.domain.model.City
import com.jdccmobile.domain.model.CityCost
import com.jdccmobile.domain.repository.CityRepository

class CityRepositoryImpl(
    private val cityDbDataSource: CityDbDataSource,
    private val costLifeDbDataSource: CostLifeDbDataSource,
    private val remoteDataSource: PlaceRemoteDataSource,
) : CityRepository {
    // Local
    // City
    override suspend fun insertCity(city: City) {
        cityDbDataSource.insertCity(city)
    }

    override suspend fun insertCitiesFromUserCountry(cities: List<City>) {
        cityDbDataSource.insertCitiesFromUserCountry(cities)
    }

    override suspend fun getCitiesFromUserCountry(countryName: String): List<City> =
        cityDbDataSource.getCitiesFromUserCountry(countryName)

    override suspend fun getCity(cityId: Int): City {
        return cityDbDataSource.getCity(cityId)
    }

    override suspend fun getFavoriteCities(): List<City> =
        cityDbDataSource.getFavoriteCities()

    override suspend fun updateCity(city: City) {
        cityDbDataSource.updateCity(city)
    }

    override suspend fun getCityCostLocal(cityId: Int): CityCost? =
        costLifeDbDataSource.getCityCostLocal(cityId)

    override suspend fun insertCityCostLocal(cityCost: CityCost) {
        costLifeDbDataSource.insertCityCost(cityCost)
    }

    // Remote
    override suspend fun getCitiesListRemote(): List<City> = remoteDataSource.getCitiesList()

    override suspend fun getCityCostRemote(cityName: String, countryName: String): CityCost =
        remoteDataSource.getCityCost(cityName, countryName)
}
