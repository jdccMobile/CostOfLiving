package com.jdccmobile.domain.repository

import com.jdccmobile.domain.model.ItemPrice
import com.jdccmobile.domain.model.Place

interface PlaceRepository {
    // Local
    suspend fun insertCity(city: Place.City)

    suspend fun insertCitiesFromUserCountry(cities: List<Place.City>)

    suspend fun getCitiesFromUserCountry(countryName: String): List<Place.City>

    suspend fun getCities(): List<Place.City>

    suspend fun getFavoriteCities(): List<Place.City>

    suspend fun updateCity(city: Place.City)


    suspend fun insertCountry(country: Place.Country)

    suspend fun getFavoriteCountries(): List<Place.Country>

    suspend fun updateCountry(country: Place.Country)


    // Remote
    suspend fun getCitiesListRemote(): List<Place.City>

    // Todo asd utilizar un mismo metodo
    suspend fun getCityCostRemote(cityName: String, countryName: String): List<ItemPrice>

    suspend fun getCountryCostRemote(countryName: String): List<ItemPrice>
}
