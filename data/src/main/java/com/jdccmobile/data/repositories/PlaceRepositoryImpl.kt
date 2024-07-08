package com.jdccmobile.data.repositories

import com.jdccmobile.data.database.datasources.CityDbDataSource
import com.jdccmobile.data.database.datasources.CountryDbDataSource
import com.jdccmobile.data.remote.datasources.PlaceRemoteDataSource
import com.jdccmobile.domain.model.ItemPrice
import com.jdccmobile.domain.model.Place
import com.jdccmobile.domain.repository.PlaceRepository

class PlaceRepositoryImpl(
    private val cityDbDataSource: CityDbDataSource,
    private val countryDbDataSource: CountryDbDataSource,
    private val remoteDataSource: PlaceRemoteDataSource,
) : PlaceRepository {
    // Local
    override suspend fun insertCity(city: Place.City) {
        cityDbDataSource.insertCity(city)
    }

    override suspend fun insertCitiesFromUserCountry(cities: List<Place.City>) {
        cityDbDataSource.insertCitiesFromUserCountry(cities)
    }

    override suspend fun getCitiesFromUserCountry(countryName: String): List<Place.City> =
        cityDbDataSource.getCitiesFromUserCountry(countryName)

    // todo creo que no se usa
    override suspend fun getCities(): List<Place.City> {
        return cityDbDataSource.getFavoriteCities()
    }

    override suspend fun getFavoriteCities(): List<Place.City> =
        cityDbDataSource.getFavoriteCities()


    override suspend fun updateCity(city: Place.City) {
        cityDbDataSource.updateCity(city)
    }


    override suspend fun insertCountry(country: Place.Country) {
        countryDbDataSource.insertCountry(country)
    }

    override suspend fun getFavoriteCountries(): List<Place.Country> =
        countryDbDataSource.getFavoriteCountries()


    override suspend fun updateCountry(country: Place.Country) {
        countryDbDataSource.updateFavoriteCountry(country)
    }

//    override suspend fun checkIsFavoritePlace(place: Place): Boolean =
//        localDataSource.checkIsFavoritePlace(place)

    // Remote
    override suspend fun getCitiesListRemote(): List<Place.City> = remoteDataSource.getCitiesList()

    // Todo asd unificar los dos ultimos
    override suspend fun getCityCostRemote(cityName: String, countryName: String): List<ItemPrice> =
        remoteDataSource.getCityCost(cityName, countryName)

    override suspend fun getCountryCostRemote(countryName: String): List<ItemPrice> =
        remoteDataSource.getCountryCost(countryName)
}
