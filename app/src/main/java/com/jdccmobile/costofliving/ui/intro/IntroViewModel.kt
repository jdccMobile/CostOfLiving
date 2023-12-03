package com.jdccmobile.costofliving.ui.intro

import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jdccmobile.costofliving.data.local.IntroSlidesProvider
import com.jdccmobile.costofliving.domain.RegionRepository
import com.jdccmobile.costofliving.model.IntroSlide
import com.jdccmobile.costofliving.ui.main.MainActivity
import kotlinx.coroutines.launch
import java.util.Locale

class IntroViewModel(
    private val activity: AppCompatActivity,
    private val regionRepository: RegionRepository,
    private val dataStore: DataStore<Preferences>
) : ViewModel(

) {
    data class UiState(
        val introSlidesInfo: List<IntroSlide> = emptyList(),
//        val countryName: String? = null
    )

    private val _state = MutableLiveData(UiState())
    val state: LiveData<UiState>
        get() {
            if (_state.value?.introSlidesInfo?.isEmpty() == true) {
                refresh()
            }
            return _state
        }

    private fun refresh() {
        viewModelScope.launch {
            _state.value = UiState(
                introSlidesInfo = IntroSlidesProvider(activity).getIntroSlides()
            )
        }
    }

    suspend fun getCountryName() {
        val countryCode = regionRepository.findLastRegion()
        val countryName = Locale("", countryCode).getDisplayCountry(Locale("EN"))
        savePreferences(countryName)
    }

    private suspend fun savePreferences(countryName: String) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey(MainActivity.COUNTRY_NAME)] = countryName
        }
    }
}


@Suppress("UNCHECKED_CAST")
class IntroViewModelFactory(
    private val activity: AppCompatActivity,
    private val regionRepository: RegionRepository,
    private val dataStore: DataStore<Preferences>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return IntroViewModel(activity, regionRepository, dataStore) as T
    }
}