package com.jdccmobile.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first

class PreferencesDataSource(private val dataStore: DataStore<Preferences>) {
    suspend fun getUserCountryPrefs(): String {
        val preferences = dataStore.data.first()
        return preferences[stringPreferencesKey(COUNTRY_NAME)] ?: ""
    }

    suspend fun putUserCountryPrefs(countryName: String) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey(COUNTRY_NAME)] = countryName
        }
    }
}

private const val COUNTRY_NAME = "country_name"
