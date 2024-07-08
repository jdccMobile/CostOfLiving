package com.jdccmobile.data.database.datasources

import com.jdccmobile.data.database.countrydb.CountryDao
import com.jdccmobile.data.utils.toDb
import com.jdccmobile.data.utils.toCountryDomain
import com.jdccmobile.domain.model.Place

class CountryDbDataSource(private val countryDao: CountryDao) {
    suspend fun insertCountry(country: Place.Country): Unit =
        countryDao.insertCountry(country.toDb())

    suspend fun getFavoriteCountries(): List<Place.Country> =
        countryDao.getFavoriteCountries().toCountryDomain()

    suspend fun updateFavoriteCountry(country: Place.Country): Unit =
        countryDao.updateFavoriteCountry(country.toDb())
}
