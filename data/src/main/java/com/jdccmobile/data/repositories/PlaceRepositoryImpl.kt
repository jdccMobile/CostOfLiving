package com.jdccmobile.data.repositories

import com.jdccmobile.data.database.datasources.PlaceLocalDataSource
import com.jdccmobile.data.remote.datasources.PlaceRemoteDataSource
import com.jdccmobile.domain.model.ItemPrice
import com.jdccmobile.domain.model.Place
import com.jdccmobile.domain.repository.PlaceRepository

class PlaceRepositoryImpl(
    private val localDataSource: PlaceLocalDataSource,
    private val remoteDataSource: PlaceRemoteDataSource,
) : PlaceRepository {
    // Local
    override suspend fun getFavoritePlacesList(): List<Place> =
        localDataSource.getFavoritePlacesList()


    override suspend fun insertFavoriteCity(city: Place): Unit =
        when(city){
            is Place.City -> {
                localDataSource.insertFavoriteCity(city)
            }
            is Place.Country -> {
                localDataSource.insertFavoriteCountry(city)
            }
        }
//        localDataSource.insertFavoriteCity(city)

    override suspend fun getFavoriteCitiesList(): List<Place.City> =
        localDataSource.getFavoriteCitiesList()

    override suspend fun deleteFavoriteCity(city: Place) = when(city){
        is Place.City -> {
            localDataSource.deleteFavoriteCity(
                cityName = city.cityName,
                countryName = city.countryName
            )
        }
        is Place.Country -> {
            localDataSource.deleteFavoriteCountry(
                countryName = city.countryName
            )
        }
    }
//        localDataSource.deleteFavoriteCity(
//            cityName = city.cityName,
//            countryName = city.countryName
//        )

    override suspend fun checkIsFavoriteCity(city: Place.City): Boolean =
        localDataSource.checkIsFavoriteCity(
            cityName = city.cityName,
            countryName = city.countryName,
        )


    override suspend fun insertFavoriteCountry(city: Place.Country) =
        localDataSource.insertFavoriteCountry(city)

    override suspend fun getFavoriteCountriesList(): List<Place.Country> =
        localDataSource.getFavoriteCountriesList()

    override suspend fun deleteFavoriteCountry(city: Place.Country): Unit =
        localDataSource.deleteFavoriteCountry(countryName = city.countryName)

    override suspend fun checkIsFavoriteCountry(city: Place.Country): Boolean =
        localDataSource.checkIsFavoriteCountry(countryName = city.countryName)

    // Remote
    override suspend fun getCitiesListRemote(): List<Place.City> = remoteDataSource.getCitiesList()

    override suspend fun getCityCostRemote(cityName: String, countryName: String): List<ItemPrice> =
        remoteDataSource.getCityCost(cityName, countryName)

    override suspend fun getCountryCostRemote(countryName: String): List<ItemPrice> =
        remoteDataSource.getCountryCost(countryName)
}
