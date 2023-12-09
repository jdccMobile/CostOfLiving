package com.jdccmobile.costofliving.ui.home.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jdccmobile.costofliving.model.Place
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DetailsViewModel(place: Place) : ViewModel() {
    data class UiState(
        val place: Place,
        val isFavorite: Boolean? = null
    )

    private val _state = MutableStateFlow(UiState(place))
    val state: StateFlow<UiState> = _state.asStateFlow()

    fun changeFavStatus() {

    }
}

@Suppress("UNCHECKED_CAST")
class DetailsViewModelFactory(private val place: Place) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailsViewModel(place) as T
    }
}