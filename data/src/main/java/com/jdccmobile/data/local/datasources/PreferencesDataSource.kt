package com.jdccmobile.data.local.datasources

import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import java.util.prefs.Preferences

class PreferencesDataSource(private val dataStore: DataStore<Preferences>) {
    companion object {
        const val COUNTRY_NAME = "country_name"
    }

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
