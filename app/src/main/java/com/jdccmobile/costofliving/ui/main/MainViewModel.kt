package com.jdccmobile.costofliving.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jdccmobile.costofliving.domain.RequestUserCountryPrefsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val requestUserCountryPrefsUseCase: RequestUserCountryPrefsUseCase,
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
    private val requestUserCountryPrefsUseCase: RequestUserCountryPrefsUseCase,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(requestUserCountryPrefsUseCase) as T
    }
}
