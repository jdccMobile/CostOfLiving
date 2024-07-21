package com.jdccmobile.costofliving.ui.features.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdccmobile.domain.usecase.GetUserCountryPrefsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val getUserCountryPrefsUseCase: GetUserCountryPrefsUseCase,
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
            getUserCountryPrefsUseCase().fold(
                {},
                { countryName ->
                    _state.value = UiState(
                        countryName = countryName,
                    )
                },
            )
        }
    }
}
