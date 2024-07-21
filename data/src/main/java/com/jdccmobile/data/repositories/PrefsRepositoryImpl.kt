package com.jdccmobile.data.repositories

import arrow.core.Either
import com.jdccmobile.data.preferences.PreferencesDataSource
import com.jdccmobile.domain.repository.PrefsRepository

class PrefsRepositoryImpl(
    private val preferencesDataSource: PreferencesDataSource,
) : PrefsRepository {
    override suspend fun getUserCountry() : Either<Throwable, String> = preferencesDataSource.getUserCountryPrefs()

    override suspend fun setUserCountry(countryName: String): Either<Throwable, Unit> =
        preferencesDataSource.putUserCountryPrefs(countryName)
}
