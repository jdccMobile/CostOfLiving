package com.jdccmobile.domain.repository

import com.jdccmobile.domain.model.ItemPrice
import com.jdccmobile.domain.model.Place

interface PlaceRepository {
    // Local
    suspend fun getFavoritePlacesList(): List<Place>

    suspend fun insertFavoriteCity(city: Place)

    suspend fun getFavoriteCitiesList(): List<Place.City>

    suspend fun deleteFavoriteCity(city: Place)

    suspend fun checkIsFavoriteCity(city: Place.City): Boolean


    suspend fun insertFavoriteCountry(city: Place.Country)

    suspend fun getFavoriteCountriesList(): List<Place.Country>

    suspend fun deleteFavoriteCountry(city: Place.Country)

    suspend fun checkIsFavoriteCountry(city: Place.Country): Boolean

    // Remote
    suspend fun getCitiesListRemote(): List<Place.City>

    suspend fun getCityCostRemote(cityName: String, countryName: String): List<ItemPrice>

    suspend fun getCountryCostRemote(countryName: String): List<ItemPrice>
}
