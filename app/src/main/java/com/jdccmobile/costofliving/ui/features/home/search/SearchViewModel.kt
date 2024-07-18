package com.jdccmobile.costofliving.ui.features.home.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import arrow.core.right
import com.jdccmobile.costofliving.R
import com.jdccmobile.costofliving.common.ResourceProvider
import com.jdccmobile.costofliving.ui.models.AutoCompleteSearchUi
import com.jdccmobile.domain.model.City
import com.jdccmobile.domain.usecase.GetCitiesFromUserCountryUseCase
import com.jdccmobile.domain.usecase.GetCitiesRemoteUseCase
import com.jdccmobile.domain.usecase.GetUserCountryPrefsUseCase
import com.jdccmobile.domain.usecase.InsertCitiesFromUserCountryUseCase
import com.jdccmobile.domain.usecase.InsertCityUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val getUserCountryPrefsUseCase: GetUserCountryPrefsUseCase,
    private val getCitiesRemoteUseCase: GetCitiesRemoteUseCase,
    private val resourceProvider: ResourceProvider,
    private val insertCitiesFromUserCountryUseCase: InsertCitiesFromUserCountryUseCase,
    private val getCitiesFromUserCountryUseCase: GetCitiesFromUserCountryUseCase,
    private val insertCityUseCase: InsertCityUseCase,
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
            getCitiesInCountry(countryName)
        }
    }

    private fun getCitiesInCountry(userCountryName: String) {
        viewModelScope.launch {
            var citiesInUserCountry: List<City> = getCitiesFromUserCountryUseCase(userCountryName)
            if (
                citiesInUserCountry.isEmpty() ||
                citiesInUserCountry.size != citiesInUserCountry[0].citiesInCountry
            ) {
                when (val citiesRemoteResult = getCitiesRemoteUseCase()) {
                    is Either.Left -> {
                        Log.i("TAG", "getCitiesInCountry: ${citiesRemoteResult.value.message}")
                        if (citiesRemoteResult.value.message?.contains("429") == true) {
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
                    }
                    is Either.Right -> {
                        citiesInUserCountry = citiesRemoteResult.value.filter {
                            it.countryName == userCountryName
                        }.sortedBy { it.cityName }
                        insertCitiesFromUserCountryUseCase(citiesInUserCountry)
                    }
                }
            }
            _state.value = _state.value.copy(apiCallCompleted = true)
            _state.value = _state.value.copy(citiesInUserCountry = citiesInUserCountry)
        }
    }

    fun onCityClicked(city: City) {
        _state.value = _state.value.copy(navigateTo = city)
    }

    fun validateSearch(nameSearch: String) {
        if (_state.value.citiesInUserCountry.any { // todo cambiar por un get de toda la base de datos
                it.cityName.equals(nameSearch, ignoreCase = true)
            }
        ) {
            _state.value = _state.value.copy(
                navigateTo = _state.value.citiesInUserCountry.find {
                    it.cityName.equals(nameSearch, ignoreCase = true)
                },
            )
        } else {
            viewModelScope.launch {
                when (val citiesRemoteResult = getCitiesRemoteUseCase()) {
                    is Either.Left -> {
                        if (citiesRemoteResult.value.message?.contains("429") == true) {
                            _state.value =
                                _state.value.copy(
                                    apiErrorMsg = resourceProvider.getString(R.string.http_429),
                                )
                        } else {
                            _state.value =
                                _state.value.copy(
                                    errorMsg = "$nameSearch ${
                                        resourceProvider.getString(R.string.does_not_exist)
                                    }",
                                )
                        }

                    }

                    is Either.Right -> {
                        if (citiesRemoteResult.value.any {
                                it.cityName.equals(nameSearch, ignoreCase = true)
                            }
                        ) {
                            val citySearched = citiesRemoteResult.value.find {
                                it.cityName.equals(nameSearch, ignoreCase = true)
                            }
                            if (citySearched != null) {
                                insertCityUseCase(citySearched)
                                _state.value = _state.value.copy(
                                    navigateTo = citySearched,
                                )
                            }
                        } else {
                            _state.value =
                                _state.value.copy(
                                    errorMsg =
                                    "$nameSearch ${
                                        resourceProvider.getString(R.string.does_not_exist)
                                    }",
                                )
                        }
                    }
                }
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
