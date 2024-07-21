package com.jdccmobile.costofliving.ui.features.home.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdccmobile.costofliving.R
import com.jdccmobile.costofliving.common.ResourceProvider
import com.jdccmobile.costofliving.common.toStringResource
import com.jdccmobile.costofliving.ui.models.CityUi
import com.jdccmobile.domain.model.City
import com.jdccmobile.domain.model.ErrorType
import com.jdccmobile.domain.usecase.GetFavoriteCitiesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val getFavoriteCitiesUseCase: GetFavoriteCitiesUseCase,
    private val resourceProvider: ResourceProvider,
) : ViewModel() {
    data class UiState(
        val favoriteCities: List<CityUi> = emptyList(),
        val navigateTo: Int? = null,
        val apiCallCompleted: Boolean = false,
        val apiErrorMsg: String? = null,
    )

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        initUi()
    }

    private fun initUi() {
        viewModelScope.launch {
            getFavoriteCities()
        }
    }

    private suspend fun getFavoriteCities() {
        getFavoriteCitiesUseCase().fold(
            { errorType ->
                _state.value = _state.value.copy(
                    apiCallCompleted = true,
                    apiErrorMsg = resourceProvider.getString(errorType.toStringResource()),
                    favoriteCities = emptyList(),
                )
            },
            { cities ->
                _state.value = _state.value.copy(favoriteCities = cities.toUi())
            },
        )

    }

    fun onCityClicked(city: City) {
        _state.value = _state.value.copy(navigateTo = city.cityId)
    }

    fun onNavigationDone() {
        _state.value = _state.value.copy(navigateTo = null)
    }
}

fun List<City>.toUi() = map { city ->
    CityUi(
        cityId = city.cityId,
        cityName = city.cityName,
        countryName = city.countryName,
        isFavorite = city.isFavorite,
    )
}
