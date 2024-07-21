package com.jdccmobile.data.preferences

import arrow.core.Either.Companion.catch
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import arrow.core.Either
import kotlinx.coroutines.flow.first

class PreferencesDataSource(private val dataStore: DataStore<Preferences>) {
    suspend fun getUserCountryPrefs(): Either<Throwable, String> = catch {
        val preferences = dataStore.data.first()
        preferences[stringPreferencesKey(COUNTRY_NAME)] ?: ""
    }


    suspend fun putUserCountryPrefs(countryName: String): Either<Throwable, Unit> = catch{
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey(COUNTRY_NAME)] = countryName
        }
    }

}

private const val COUNTRY_NAME = "country_name"
