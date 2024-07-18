package com.jdccmobile.costofliving.ui.features.home.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdccmobile.costofliving.ui.models.CityUi
import com.jdccmobile.costofliving.ui.utils.toCityUi
import com.jdccmobile.domain.model.City
import com.jdccmobile.domain.usecase.GetFavoriteCitiesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val getFavoriteCitiesUseCase: GetFavoriteCitiesUseCase,
) : ViewModel() {
    data class UiState(
        val favoriteCities: List<CityUi> = emptyList(),
        val navigateTo: Int? = null,
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
        _state.value = _state.value.copy(favoriteCities = getFavoriteCitiesUseCase().toCityUi())
    }

    fun onCityClicked(city: City) {
        _state.value = _state.value.copy(navigateTo = city.cityId)
    }

    fun onNavigationDone() {
        _state.value = _state.value.copy(navigateTo = null)
    }
}
