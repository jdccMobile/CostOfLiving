package com.jdccmobile.data.repositories

import com.jdccmobile.data.preferences.PreferencesDataSource
import com.jdccmobile.domain.repository.PrefsRepository

class PrefsRepositoryImpl(
    private val preferencesDataSource: PreferencesDataSource,
) : PrefsRepository {
    override suspend fun getUserCountry(): String = preferencesDataSource.getUserCountryPrefs()

    override suspend fun setUserCountry(countryName: String): Unit =
        preferencesDataSource.putUserCountryPrefs(countryName)
}
