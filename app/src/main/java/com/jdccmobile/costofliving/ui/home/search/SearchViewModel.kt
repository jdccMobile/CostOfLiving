package com.jdccmobile.costofliving.ui.home.search

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jdccmobile.costofliving.data.remote.CostInfoRepository
import com.jdccmobile.costofliving.data.remote.model.City
import com.jdccmobile.costofliving.model.AutoCompleteSearch
import com.jdccmobile.costofliving.ui.main.MainActivity.Companion.COUNTRY_NAME
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SearchViewModel(
    private val dataStore: DataStore<Preferences>,
    private val costInfoRepository: CostInfoRepository
) : ViewModel() {
    data class UiState(
        val citiesLoaded: Boolean = false,
        val countryName: String = "",
        val citiesInUserCountry: List<City> = emptyList(),
        val citiesAutoComplete: List<AutoCompleteSearch> = emptyList(),
        val countriesAutoComplete: List<AutoCompleteSearch> = emptyList(),
    )

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()
    init {
        refresh()
    }

    private fun refresh() {
        viewModelScope.launch{
            val countryName = getPreferences(COUNTRY_NAME)
            _state.value = UiState(citiesLoaded = true, countryName = countryName)
            createLists(countryName)
        }
    }

    private suspend fun getPreferences(key: String): String {
        val preferences = dataStore.data.first()
        return preferences[stringPreferencesKey(key)] ?: ""
    }

    private suspend fun createLists(userCountryName: String) {
        val citiesList = costInfoRepository.getCities().cities
        val citiesInUserCountry = citiesList.filter { it.countryName == userCountryName }
        _state.value = _state.value.copy(citiesInUserCountry = citiesInUserCountry)

        val citiesAutoComplete =
            citiesList.map { AutoCompleteSearch(it.cityName, it.countryName) }
        _state.value = _state.value.copy(citiesAutoComplete = citiesAutoComplete)

        val countriesAutoComplete =
            citiesList.distinctBy { it.countryName }
                .map { AutoCompleteSearch(it.countryName, it.countryName) }
        _state.value = _state.value.copy(countriesAutoComplete = countriesAutoComplete)
    }
}

@Suppress("UNCHECKED_CAST")
class SearchViewModelFactory(private val dataStore: DataStore<Preferences>, private val costInfoRepository: CostInfoRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(dataStore, costInfoRepository) as T
    }
}