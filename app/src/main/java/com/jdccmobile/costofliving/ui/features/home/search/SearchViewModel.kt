package com.jdccmobile.costofliving.ui.features.home.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdccmobile.costofliving.R
import com.jdccmobile.costofliving.common.ResourceProvider
import com.jdccmobile.costofliving.ui.models.AutoCompleteSearchUi
import com.jdccmobile.costofliving.ui.models.PlaceUi
import com.jdccmobile.costofliving.ui.models.toUi
import com.jdccmobile.domain.usecase.GetCityListUseCase
import com.jdccmobile.domain.usecase.GetUserCountryPrefsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val getUserCountryPrefsUseCase: GetUserCountryPrefsUseCase,
    private val getCityListUseCase: GetCityListUseCase,
    private val resourceProvider: ResourceProvider,
) : ViewModel() {
    data class UiState(
        val apiCallCompleted: Boolean = false,
        val countryName: String? = null,
        val isSearchByCity: Boolean = true,
        val citiesInUserCountry: List<PlaceUi.City> = emptyList(),
        val citiesAutoComplete: List<AutoCompleteSearchUi> =
            emptyList(),
        val countriesAutoComplete: List<AutoCompleteSearchUi> =
            emptyList(),
        val navigateTo: PlaceUi? = null,
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
            val countryName = getUserCountryPrefsUseCase()
            _state.value = UiState(countryName = countryName)
            createLists(countryName)
        }
    }

    private suspend fun createLists(userCountryName: String) {
        if (!_state.value.apiCallCompleted) {
            try {
                Log.i("JD Search VM", "API call: requestCitiesList")
                val citiesList = getCityListUseCase()
                val citiesInUserCountry = citiesList.filter {
                    it.countryName == userCountryName
                }.sortedBy { it.cityName }.toUi()
                _state.value = _state.value.copy(citiesInUserCountry = citiesInUserCountry)

                val citiesAutoComplete =
                    citiesList.map {
                        AutoCompleteSearchUi(
                            it.cityName,
                            it.countryName,
                        )
                    }
                _state.value = _state.value.copy(citiesAutoComplete = citiesAutoComplete)

                val countriesAutoComplete =
                    citiesList.distinctBy { it.countryName }
                        .map {
                            AutoCompleteSearchUi(
                                it.countryName,
                                it.countryName,
                            )
                        }
                _state.value = _state.value.copy(countriesAutoComplete = countriesAutoComplete)
            } catch (e: Exception) {
                if (e.message?.contains("429") == true) {
                    _state.value =
                        _state.value.copy(
                            apiErrorMsg = resourceProvider.getString(R.string.http_429),
                        )
                } else {
                    _state.value =
                        _state.value.copy(
                            apiErrorMsg = resourceProvider.getString(R.string.connection_error),
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

    fun onCityClicked(place: PlaceUi) {
        _state.value = _state.value.copy(navigateTo = place)
    }

    fun validateSearch(nameSearch: String) {
        if (_state.value.isSearchByCity) {
            if (_state.value.citiesAutoComplete.any {
                    it.searchedText.equals(nameSearch, ignoreCase = true)
                }
            ) {
                val countryName = _state.value.citiesAutoComplete.find {
                    it.searchedText.equals(nameSearch, ignoreCase = true)
                }?.country ?: ""
                _state.value = _state.value.copy(navigateTo = PlaceUi.City(nameSearch, countryName))
            } else {
                _state.value =
                    _state.value.copy(
                        errorMsg =
                            "$nameSearch ${resourceProvider.getString(R.string.does_not_exist)}",
                    )
            }
        } else {
            if (_state.value.countriesAutoComplete.any {
                    it.searchedText.equals(nameSearch, ignoreCase = true)
                }
            ) {
                _state.value =
                    _state.value.copy(navigateTo = PlaceUi.Country(countryName = nameSearch))
            } else {
                _state.value =
                    _state.value.copy(
                        errorMsg =
                            "$nameSearch ${resourceProvider.getString(R.string.does_not_exist)}",
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
