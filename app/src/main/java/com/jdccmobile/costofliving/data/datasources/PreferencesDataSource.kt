package com.jdccmobile.costofliving.data.datasources

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first

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
