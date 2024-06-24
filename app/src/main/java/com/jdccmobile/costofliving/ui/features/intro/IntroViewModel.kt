package com.jdccmobile.costofliving.ui.features.intro

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jdccmobile.costofliving.data.repositories.RegionRepository
import com.jdccmobile.data.repositories.CostInfoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale

class IntroViewModel(
    private val activity: AppCompatActivity,
    private val regionRepository: RegionRepository,
    private val costInfoRepository: com.jdccmobile.data.repositories.CostInfoRepository,
) : ViewModel() {
    data class UiState(
        val introSlidesInfo: List<com.jdccmobile.domain.model.IntroSlide> = emptyList(),
    )

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        refresh()
    }

    private fun refresh() {
        viewModelScope.launch {
            _state.value = UiState(
                introSlidesInfo = IntroSlidesProvider(activity).getIntroSlides(),
            )
        }
    }

    suspend fun getCountryName() {
        val countryCode = regionRepository.findLastRegion()
        val countryName = Locale("", countryCode).getDisplayCountry(Locale("EN"))
        costInfoRepository.saveUserCountryPrefs(countryName)
    }
}

@Suppress("UNCHECKED_CAST")
class IntroViewModelFactory(
    private val activity: AppCompatActivity,
    private val regionRepository: RegionRepository,
    private val costInfoRepository: com.jdccmobile.data.repositories.CostInfoRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return IntroViewModel(activity, regionRepository, costInfoRepository) as T
    }
}
