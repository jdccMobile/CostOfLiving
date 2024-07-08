package com.jdccmobile.data.database.datasources

import com.jdccmobile.data.database.citydb.CityDao
import com.jdccmobile.data.utils.toDb
import com.jdccmobile.data.utils.toCityDomain
import com.jdccmobile.domain.model.Place

class CityDbDataSource(private val cityDao: CityDao) {
    suspend fun insertCity(city: Place.City): Unit =
        cityDao.insertCity(city.toDb())

    suspend fun insertCitiesFromUserCountry(cities: List<Place.City>): Unit =
        cityDao.insertCitiesFromUserCountry(cities.toDb())

    suspend fun getCitiesFromUserCountry(countryName: String): List<Place.City> =
        cityDao.getCitiesFromUserCountry(countryName).toCityDomain()

    suspend fun getFavoriteCities(): List<Place.City> =
        cityDao.getCityList().toCityDomain()

    suspend fun updateCity(city: Place.City): Unit =
        cityDao.updateCity(city.toDb())
}
