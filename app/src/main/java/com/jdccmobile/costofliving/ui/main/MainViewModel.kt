package com.jdccmobile.costofliving.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jdccmobile.costofliving.domain.usecases.RequestUserCountryPrefsUC
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val requestUserCountryPrefsUC: RequestUserCountryPrefsUC
): ViewModel() {

    data class UiState(
        val countryName: String? = null,
    )
    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        refresh()
    }

    private fun refresh() {
        viewModelScope.launch{
            _state.value = UiState(
                requestUserCountryPrefsUC()
            )
        }
    }
}
