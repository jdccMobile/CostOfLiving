package com.jdccmobile.costofliving.ui.features.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val requestUserCountryPrefsUseCase: com.jdccmobile.domain.usecase.RequestUserCountryPrefsUseCase,
) : ViewModel() {
    data class UiState(
        val countryName: String? = null,
    )

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        refresh()
    }

    private fun refresh() {
        viewModelScope.launch {
            _state.value = UiState(
                requestUserCountryPrefsUseCase(),
            )
        }
    }
}

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(
    private val requestUserCountryPrefsUseCase: com.jdccmobile.domain.usecase.RequestUserCountryPrefsUseCase,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(requestUserCountryPrefsUseCase) as T
    }
}
