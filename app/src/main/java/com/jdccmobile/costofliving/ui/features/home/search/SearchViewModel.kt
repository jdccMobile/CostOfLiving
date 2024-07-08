package com.jdccmobile.costofliving.ui.features.home.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdccmobile.costofliving.R
import com.jdccmobile.costofliving.common.ResourceProvider
import com.jdccmobile.costofliving.ui.models.AutoCompleteSearchUi
import com.jdccmobile.domain.model.City
import com.jdccmobile.domain.usecase.GetCitiesRemote
import com.jdccmobile.domain.usecase.GetUserCountryPrefsUseCase
import com.jdccmobile.domain.usecase.InsertCitiesFromUserCountryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val getUserCountryPrefsUseCase: GetUserCountryPrefsUseCase,
    private val getCitiesRemote: GetCitiesRemote,
    private val resourceProvider: ResourceProvider,
    private val insertCitiesFromUserCountryUseCase: InsertCitiesFromUserCountryUseCase,
) : ViewModel() {
    data class UiState(
        val apiCallCompleted: Boolean = false,
        val countryName: String? = null,
        val isSearchByCity: Boolean = true,
        val citiesInUserCountry: List<City> = emptyList(),
        val citiesAutoComplete: List<AutoCompleteSearchUi> =
            emptyList(),
        val countriesAutoComplete: List<AutoCompleteSearchUi> =
            emptyList(),
        val navigateTo: City? = null,
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

    @Suppress("TooGenericExceptionCaught")
    private suspend fun createLists(userCountryName: String) {
        if (!_state.value.apiCallCompleted) {
            try {
                // todo asd acceder a room y mirar si tengo todas las ciudades del pais, comprobar que viene lo mismo que de api
                Log.i("JD Search VM", "API call: requestCitiesList")
                val citiesList = getCitiesRemote()
                val citiesInUserCountry = citiesList.filter {
                    it.countryName == userCountryName
                }.sortedBy { it.cityName }

                _state.value = _state.value.copy(
                    citiesInUserCountry = citiesInUserCountry,
                )
                insertCitiesFromUserCountryUseCase(citiesInUserCountry)

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

    fun onCityClicked(city: City) {
        _state.value = _state.value.copy(navigateTo = city)
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
                _state.value = _state.value.copy(
                    navigateTo = City(
                        cityId = 1,
                        countryName = nameSearch,
                        cityName = countryName,
                    ),
                ) // todo asd
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
//                _state.value =
//                    _state.value.copy(navigateTo = Country(countryName = nameSearch, countryId = "")) // todo asd
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
