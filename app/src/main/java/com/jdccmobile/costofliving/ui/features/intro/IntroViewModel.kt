package com.jdccmobile.costofliving.ui.features.intro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdccmobile.data.repositories.PrefsRepositoryImpl
import com.jdccmobile.data.repositories.RegionRepository
import com.jdccmobile.domain.model.IntroSlide
import com.jdccmobile.domain.usecase.InsertUserCountryPrefsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale

class IntroViewModel(
    private val regionRepository: RegionRepository,
    private val introSlidesProvider: IntroSlidesProvider,
    private val insertUserCountryPrefsUseCase: InsertUserCountryPrefsUseCase,
    ) : ViewModel() {
    data class UiState(
        val introSlidesInfo: List<IntroSlide> = emptyList(),
    )

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        refresh()
    }

    private fun refresh() {
        viewModelScope.launch {
            _state.value = UiState(
                introSlidesInfo = introSlidesProvider.getIntroSlides(),
            )
        }
    }

    suspend fun getCountryName() {
        val countryCode = regionRepository.findLastRegion()
        val countryName = Locale("", countryCode).getDisplayCountry(Locale("EN"))
        insertUserCountryPrefsUseCase(countryName)
    }
}
