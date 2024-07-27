package com.jdccmobile.costofliving.ui.features.home.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdccmobile.costofliving.R
import com.jdccmobile.costofliving.common.ResourceProvider
import com.jdccmobile.costofliving.common.toStringResource
import com.jdccmobile.domain.model.City
import com.jdccmobile.domain.usecase.GetCitiesFromUserCountryUseCase
import com.jdccmobile.domain.usecase.GetCitiesUseCase
import com.jdccmobile.domain.usecase.GetUserCountryPrefsUseCase
import com.jdccmobile.domain.usecase.InsertCityUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel(
    private val getUserCountryPrefsUseCase: GetUserCountryPrefsUseCase,
    private val getCitiesUseCase: GetCitiesUseCase,
    private val resourceProvider: ResourceProvider,
    private val getCitiesFromUserCountryUseCase: GetCitiesFromUserCountryUseCase,
    private val insertCityUseCase: InsertCityUseCase,
) : ViewModel() {
    data class UiState(
        val apiCallCompleted: Boolean = false,
        val countryName: String? = null,
        val searchType: SearchType = SearchType.Fast,
        val fastAccessibleCities: List<City> = emptyList(),
        val navigateTo: City? = null,
        val errorMsg: String? = null,
        val apiErrorMsg: String? = null,
    )

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    private var citiesFromUserCountry = emptyList<City>()

    init {
        refresh()
    }

    private fun refresh() {
        viewModelScope.launch {
            getUserCountryPrefsUseCase().fold(
                {},
                { countryName ->
                    _state.value = UiState(countryName = countryName)
                    getCitiesInUserCountry(countryName)
                },
            )
        }
    }

    fun onCityClicked(city: City) {
        when (_state.value.searchType) {
            SearchType.Fast -> _state.value = _state.value.copy(navigateTo = city)
            SearchType.Complete -> {
                viewModelScope.launch {
                    insertNewCityToDb(city)
                    withContext(Dispatchers.Main) {
                        _state.value = _state.value.copy(navigateTo = city)
                    }
                }
            }
        }
    }

    fun changeSearchType(searchType: SearchType) {
        _state.value = _state.value.copy(searchType = searchType)
        when (_state.value.searchType) {
            SearchType.Fast ->
                _state.value =
                    _state.value.copy(fastAccessibleCities = citiesFromUserCountry)

            SearchType.Complete ->
                _state.value =
                    _state.value.copy(fastAccessibleCities = emptyList())
        }
    }

    fun validateSearch(nameSearch: String) {
        when (_state.value.searchType) {
            SearchType.Fast -> {
                viewModelScope.launch {
                    getCitiesUseCase().fold(
                        { error -> setErrorMessage(error) },
                        { cities ->
                            cities.find {
                                it.cityName.equals(nameSearch, ignoreCase = true)
                            }?.let { _state.value = _state.value.copy(navigateTo = it) }
                                ?: run {
                                    searchCityInApi(nameSearch)
                                }
                        },
                    )
                }
            }

            SearchType.Complete -> {
                viewModelScope.launch {
                    getCitiesUseCase(false).fold(
                        { error -> setErrorMessage(error) },
                        { cities ->
                            if (cities.any { it.cityName.equals(nameSearch, ignoreCase = true) }) {
                                val allCoincidencesCities = cities.filter {
                                    it.cityName.equals(nameSearch, ignoreCase = true)
                                }
                                _state.value =
                                    _state.value.copy(fastAccessibleCities = allCoincidencesCities)
                            }
                        },
                    )
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

    private suspend fun insertNewCityToDb(city: City) {
        insertCityUseCase(city).fold(
            { error -> setErrorMessage(error) },
            {
                _state.value =
                    _state.value.copy(navigateTo = city)
            },
        )
    }

    private fun searchCityInApi(nameSearch: String) = viewModelScope.launch {
        getCitiesUseCase(false).fold(
            { error -> setErrorMessage(error) },
            { cities ->
                cities.find { it.cityName.equals(nameSearch, ignoreCase = true) }
                    ?.let { insertNewCityToDb(it) }
                    ?: run {
                        _state.value =
                            _state.value.copy(
                                errorMsg = "$nameSearch ${
                                    resourceProvider.getString(
                                        R.string.does_not_exist,
                                    )
                                }",
                            )
                    }
            },
        )
    }

    private fun getCitiesInUserCountry(userCountryName: String) {
        viewModelScope.launch {
            getCitiesFromUserCountryUseCase(userCountryName).fold(
                { error ->
                    _state.value = _state.value.copy(
                        apiCallCompleted = true,
                        apiErrorMsg = resourceProvider.getString(error.toStringResource()),
                        fastAccessibleCities = emptyList(),
                    )
                },
                { cities ->
                    citiesFromUserCountry = cities
                    _state.value = _state.value.copy(
                        apiCallCompleted = true,
                        fastAccessibleCities = cities,
                    )
                },
            )
        }
    }

    private fun setErrorMessage(error: Throwable) {
        _state.value = _state.value.copy(
            errorMsg = resourceProvider.getString(error.toStringResource()),
        )
    }
}

enum class SearchType {
    Fast,
    Complete,
}
