package com.jdccmobile.costofliving.ui.home.search

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jdccmobile.costofliving.data.CostInfoRepository
import com.jdccmobile.costofliving.data.remote.model.citieslist.City
import com.jdccmobile.costofliving.model.AutoCompleteSearch
import com.jdccmobile.costofliving.model.Place
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
        val countryName: String? = null,
        val isSearchByCity: Boolean = true,
        val citiesInUserCountry: List<City> = emptyList(),
        val citiesAutoComplete: List<AutoCompleteSearch> = emptyList(),
        val countriesAutoComplete: List<AutoCompleteSearch> = emptyList(),
        val navigateTo: Place? = null,
        val errorMsg: String? = null
    )

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()
    init { refresh() }

    private fun refresh() {
        viewModelScope.launch{
            val countryName = getPreferences(COUNTRY_NAME)
            _state.value = UiState(countryName = countryName)
            createLists(countryName)
        }
    }

    private suspend fun getPreferences(key: String): String {
        val preferences = dataStore.data.first()
        return preferences[stringPreferencesKey(key)] ?: ""
    }

    private suspend fun createLists(userCountryName: String) {
        if(!_state.value.citiesLoaded){
            Log.i("JD Search VM", "API call")
            val citiesList = costInfoRepository.requestCitiesList().cities
            val citiesInUserCountry = citiesList.filter { it.countryName == userCountryName }.sortedBy { it.cityName }
            _state.value = _state.value.copy(citiesInUserCountry = citiesInUserCountry)

            val citiesAutoComplete =
                citiesList.map { AutoCompleteSearch(it.cityName, it.countryName) }
            _state.value = _state.value.copy(citiesAutoComplete = citiesAutoComplete)

            val countriesAutoComplete =
                citiesList.distinctBy { it.countryName }
                    .map { AutoCompleteSearch(it.countryName, it.countryName) }
            _state.value = _state.value.copy(citiesLoaded = true, countriesAutoComplete = countriesAutoComplete)
        }
    }

    fun changeSearchByCity(isSearchByCity: Boolean){
        _state.value = _state.value.copy(isSearchByCity = isSearchByCity)
    }

    fun onPlaceClicked(place: Place){
        _state.value = _state.value.copy(navigateTo = place)
    }

    fun validateSearch(nameSearch: String) {
        if (_state.value.isSearchByCity) {
            if (_state.value.citiesAutoComplete.any {
                    it.textSearch.equals(nameSearch, ignoreCase = true)
                }) {
                val countryName = _state.value.citiesAutoComplete.find {
                        it.textSearch.equals(nameSearch, ignoreCase = true)
                    }?.country ?: ""
                _state.value = _state.value.copy(navigateTo = Place(nameSearch, countryName))
            } else {
                _state.value = _state.value.copy(errorMsg = "$nameSearch does not exist")
            }

        } else {
            if (_state.value.countriesAutoComplete.any {
                    it.textSearch.equals(nameSearch, ignoreCase = true)
                }) {
                _state.value = _state.value.copy(navigateTo = Place(countryName = nameSearch))
            } else {
                _state.value = _state.value.copy(errorMsg = "$nameSearch does not exist")
            }
        }
    }


    fun onNavigationDone() {
        _state.value = _state.value.copy(navigateTo = null)
    }

    fun onErrorMsgShown(){
        _state.value = _state.value.copy(errorMsg = null)
    }
}

@Suppress("UNCHECKED_CAST")
class SearchViewModelFactory(private val dataStore: DataStore<Preferences>, private val costInfoRepository: CostInfoRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(dataStore, costInfoRepository) as T
    }
}