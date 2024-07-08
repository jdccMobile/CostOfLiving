package com.jdccmobile.data.database.datasources

import com.jdccmobile.data.database.citydb.CityDao
import com.jdccmobile.data.database.citydb.toDb
import com.jdccmobile.data.database.citydb.toDomain
import com.jdccmobile.domain.model.City

class CityDbDataSource(private val cityDao: CityDao) {
    suspend fun insertCity(city: City): Unit =
        cityDao.insertCity(city.toDb())

    suspend fun insertCitiesFromUserCountry(cities: List<City>): Unit =
        cityDao.insertCitiesFromUserCountry(cities.toDb())

    suspend fun getCitiesFromUserCountry(countryName: String): List<City> =
        cityDao.getCitiesFromUserCountry(countryName).toDomain()

    suspend fun getFavoriteCities(): List<City> =
        cityDao.getCityList().toDomain()

    suspend fun updateCity(city: City): Unit =
        cityDao.updateCity(city.toDb())
}
