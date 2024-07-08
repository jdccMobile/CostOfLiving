package com.jdccmobile.data.database.datasources

import com.jdccmobile.data.database.countrydb.CountryDao
import com.jdccmobile.data.database.countrydb.toDb
import com.jdccmobile.data.database.countrydb.toDomain
import com.jdccmobile.domain.model.Country

class CountryDbDataSource(private val countryDao: CountryDao) {
    suspend fun insertCountry(country: Country): Unit =
        countryDao.insertCountry(country.toDb())

    suspend fun getFavoriteCountries(): List<Country> =
        countryDao.getFavoriteCountries().toDomain()

    suspend fun updateFavoriteCountry(country: Country): Unit =
        countryDao.updateFavoriteCountry(country.toDb())
}
