package com.jdccmobile.costofliving.ui.features.home.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdccmobile.costofliving.ui.models.PlaceUi
import com.jdccmobile.costofliving.ui.models.toUi
import com.jdccmobile.domain.usecase.GetFavoriteCitiesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val getFavoriteCitiesUseCase: GetFavoriteCitiesUseCase,
) : ViewModel() {
    data class UiState(
        val placeList: List<PlaceUi> = emptyList(),
        val navigateTo: PlaceUi? = null,
    )

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        refresh()
    }

    private fun refresh() {
        viewModelScope.launch {
            createCityList()
        }
    }

    private suspend fun createCityList() {
        _state.value = _state.value.copy(placeList = getFavoriteCitiesUseCase().toUi())
    }

    fun onCityClicked(place: PlaceUi) {
        _state.value = _state.value.copy(navigateTo = place)
    }

    fun onNavigationDone() {
        _state.value = _state.value.copy(navigateTo = null)
    }
}
