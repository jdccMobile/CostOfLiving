package com.jdccmobile.costofliving.ui.intro

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jdccmobile.costofliving.data.local.IntroSlidesProvider
import com.jdccmobile.costofliving.domain.usecases.FindLastRegionUC
import com.jdccmobile.costofliving.domain.usecases.SaveUserCountryPrefsUC
import com.jdccmobile.costofliving.domain.model.IntroSlide
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale

class IntroViewModel(
    private val activity: AppCompatActivity,
    private val saveUserCountryPrefsUC: SaveUserCountryPrefsUC,
    private val findLastRegionUC: FindLastRegionUC
) : ViewModel(

) {
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
                introSlidesInfo = IntroSlidesProvider(activity).getIntroSlides()
            )
        }
    }

    suspend fun getCountryName() {
        val countryCode = findLastRegionUC()
        val countryName = Locale("", countryCode).getDisplayCountry(Locale("EN"))
        saveUserCountryPrefsUC(countryName)
    }
}


@Suppress("UNCHECKED_CAST")
class IntroViewModelFactory(
    private val activity: AppCompatActivity,
    private val saveUserCountryPrefsUC: SaveUserCountryPrefsUC,
    private val findLastRegionUC: FindLastRegionUC
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return IntroViewModel(activity, saveUserCountryPrefsUC, findLastRegionUC) as T
    }
}