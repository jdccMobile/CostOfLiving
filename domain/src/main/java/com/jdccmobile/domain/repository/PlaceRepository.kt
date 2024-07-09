package com.jdccmobile.domain.repository

import com.jdccmobile.domain.model.City
import com.jdccmobile.domain.model.Country
import com.jdccmobile.domain.model.ItemPrice

interface PlaceRepository {
    // Local
    suspend fun insertCity(city: City)

    suspend fun insertCitiesFromUserCountry(cities: List<City>)

    suspend fun getCitiesFromUserCountry(countryName: String): List<City>

    suspend fun getCity(cityId: Int): City

    suspend fun getFavoriteCities(): List<City>

    suspend fun updateCity(city: City)

    suspend fun insertCountry(country: Country)

    suspend fun getFavoriteCountries(): List<Country>

    suspend fun updateCountry(country: Country)

    // Remote
    suspend fun getCitiesListRemote(): List<City>

    // Todo asd utilizar un mismo metodo
    suspend fun getCityCostRemote(cityName: String, countryName: String): List<ItemPrice>

    suspend fun getCountryCostRemote(countryName: String): List<ItemPrice>
}
