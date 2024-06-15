package com.jdccmobile.costofliving.ui.home.search

import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jdccmobile.costofliving.R
import com.jdccmobile.costofliving.data.remote.model.citieslist.City
import com.jdccmobile.costofliving.domain.RequestCitiesListUseCase
import com.jdccmobile.costofliving.domain.RequestUserCountryPrefsUseCase
import com.jdccmobile.costofliving.model.AutoCompleteSearch
import com.jdccmobile.costofliving.model.Place
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val fragment: FragmentActivity,
    private val requestUserCountryPrefsUseCase: RequestUserCountryPrefsUseCase,
    private val requestCitiesListUseCase: RequestCitiesListUseCase,
) : ViewModel() {
    data class UiState(
        val apiCallCompleted: Boolean = false,
        val countryName: String? = null,
        val isSearchByCity: Boolean = true,
        val citiesInUserCountry: List<City> = emptyList(),
        val citiesAutoComplete: List<AutoCompleteSearch> = emptyList(),
        val countriesAutoComplete: List<AutoCompleteSearch> = emptyList(),
        val navigateTo: Place? = null,
        val errorMsg: String? = null,
        val apiErrorMsg: String? = null,
    )

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        refresh()
    }

    private fun refresh() {
        viewModelScope.launch {
            val countryName = requestUserCountryPrefsUseCase()
            _state.value = UiState(countryName = countryName)
            createLists(countryName)
        }
    }

    @Suppress("TooGenericExceptionCaught")
    private suspend fun createLists(userCountryName: String) {
        if (!_state.value.apiCallCompleted) {
            try {
                Log.i("JD Search VM", "API call: requestCitiesList")
                val citiesList = requestCitiesListUseCase().cities
                val citiesInUserCountry = citiesList.filter {
                    it.countryName == userCountryName
                }.sortedBy { it.cityName }
                _state.value = _state.value.copy(citiesInUserCountry = citiesInUserCountry)

                val citiesAutoComplete =
                    citiesList.map { AutoCompleteSearch(it.cityName, it.countryName) }
                _state.value = _state.value.copy(citiesAutoComplete = citiesAutoComplete)

                val countriesAutoComplete =
                    citiesList.distinctBy { it.countryName }
                        .map { AutoCompleteSearch(it.countryName, it.countryName) }
                _state.value = _state.value.copy(countriesAutoComplete = countriesAutoComplete)
            } catch (e: Exception) {
                if (e.message?.contains("429") == true) {
                    _state.value = _state.value.copy(
                        apiErrorMsg = fragment.getString(R.string.http_429),
                    )
                } else {
                    _state.value = _state.value.copy(
                        apiErrorMsg = fragment.getString(R.string.connection_error),
                    )
                }
                Log.e("JD Search VM", "API call requestCitiesList error: $e")
            }
            _state.value = _state.value.copy(apiCallCompleted = true)
        }
    }

    fun changeSearchByCity(isSearchByCity: Boolean) {
        _state.value = _state.value.copy(isSearchByCity = isSearchByCity)
    }

    fun onPlaceClicked(place: Place) {
        _state.value = _state.value.copy(navigateTo = place)
    }

    fun validateSearch(nameSearch: String) {
        if (_state.value.isSearchByCity) {
            if (_state.value.citiesAutoComplete.any {
                    it.textSearch.equals(nameSearch, ignoreCase = true)
                }
            ) {
                val countryName = _state.value.citiesAutoComplete.find {
                    it.textSearch.equals(nameSearch, ignoreCase = true)
                }?.country ?: ""
                _state.value = _state.value.copy(navigateTo = Place(nameSearch, countryName))
            } else {
                _state.value = _state.value.copy(
                    errorMsg = "$nameSearch ${fragment.getString(R.string.does_not_exist)}",
                )
            }
        } else {
            if (_state.value.countriesAutoComplete.any {
                    it.textSearch.equals(nameSearch, ignoreCase = true)
                }
            ) {
                _state.value = _state.value.copy(navigateTo = Place(countryName = nameSearch))
            } else {
                _state.value = _state.value.copy(
                    errorMsg = "$nameSearch ${fragment.getString(R.string.does_not_exist)}",
                )
            }
        }
    }

    fun onNavigationDone() {
        _state.value = _state.value.copy(navigateTo = null)
    }

    fun onErrorMsgShown() {
        _state.value = _state.value.copy(errorMsg = null)
    }

    fun onApiErrorMsgShown() {
        _state.value = _state.value.copy(apiErrorMsg = null)
    }
}

@Suppress("UNCHECKED_CAST")
class SearchViewModelFactory(
    private val fragment: FragmentActivity,
    private val requestUserCountryPrefsUseCase: RequestUserCountryPrefsUseCase,
    private val requestCitiesListUseCase: RequestCitiesListUseCase,
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(
            fragment,
            requestUserCountryPrefsUseCase,
            requestCitiesListUseCase,
        ) as T
    }
}
