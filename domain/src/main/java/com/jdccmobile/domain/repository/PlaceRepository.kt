package com.jdccmobile.domain.repository

import com.jdccmobile.domain.model.ItemPrice
import com.jdccmobile.domain.model.Place

interface PlaceRepository {
    suspend fun insertFavoriteCity(city: Place.City)

    suspend fun getFavoriteCitiesList(): List<Place.City>

    suspend fun deleteFavoriteCity(city: Place.City)

    suspend fun getCitiesListRemote(): List<Place.City>

    suspend fun getCityCostRemote(cityName: String, countryName: String): List<ItemPrice>

    suspend fun getCountryCostRemote(countryName: String): List<ItemPrice>
}
