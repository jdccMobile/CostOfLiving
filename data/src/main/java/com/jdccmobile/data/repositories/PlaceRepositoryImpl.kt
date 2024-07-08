package com.jdccmobile.data.repositories

import com.jdccmobile.data.database.datasources.CityDbDataSource
import com.jdccmobile.data.database.datasources.CountryDbDataSource
import com.jdccmobile.data.remote.datasources.PlaceRemoteDataSource
import com.jdccmobile.domain.model.City
import com.jdccmobile.domain.model.Country
import com.jdccmobile.domain.model.ItemPrice
import com.jdccmobile.domain.repository.PlaceRepository

class PlaceRepositoryImpl(
    private val cityDbDataSource: CityDbDataSource,
    private val countryDbDataSource: CountryDbDataSource,
    private val remoteDataSource: PlaceRemoteDataSource,
) : PlaceRepository {
    // Local
    override suspend fun insertCity(city: City) {
        cityDbDataSource.insertCity(city)
    }

    override suspend fun insertCitiesFromUserCountry(cities: List<City>) {
        cityDbDataSource.insertCitiesFromUserCountry(cities)
    }

    override suspend fun getCitiesFromUserCountry(countryName: String): List<City> =
        cityDbDataSource.getCitiesFromUserCountry(countryName)

    // todo creo que no se usa
    override suspend fun getCities(): List<City> {
        return cityDbDataSource.getFavoriteCities()
    }

    override suspend fun getFavoriteCities(): List<City> =
        cityDbDataSource.getFavoriteCities()

    override suspend fun updateCity(city: City) {
        cityDbDataSource.updateCity(city)
    }

    override suspend fun insertCountry(country: Country) {
        countryDbDataSource.insertCountry(country)
    }

    override suspend fun getFavoriteCountries(): List<Country> =
        countryDbDataSource.getFavoriteCountries()

    override suspend fun updateCountry(country: Country) {
        countryDbDataSource.updateFavoriteCountry(country)
    }

//    override suspend fun checkIsFavoritePlace(place: Place): Boolean =
//        localDataSource.checkIsFavoritePlace(place)

    // Remote
    override suspend fun getCitiesListRemote(): List<City> = remoteDataSource.getCitiesList()

    // Todo asd unificar los dos ultimos
    override suspend fun getCityCostRemote(cityName: String, countryName: String): List<ItemPrice> =
        remoteDataSource.getCityCost(cityName, countryName)

    override suspend fun getCountryCostRemote(countryName: String): List<ItemPrice> =
        remoteDataSource.getCountryCost(countryName)
}
