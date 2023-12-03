package com.jdccmobile.costofliving.ui.main

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jdccmobile.costofliving.ui.main.MainActivity.Companion.COUNTRY_NAME
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainViewModel(
    private val dataStore: DataStore<Preferences>
): ViewModel() {

    data class UiState(
        val countryName: String? = null,
    )

    private val _state = MutableLiveData(UiState())
    val state: LiveData<UiState> get() {
        if (_state.value?.countryName == null){
            refresh()
        }
        return _state
    }

    private fun refresh() {
        viewModelScope.launch{
            _state.value = UiState(getPreferences(COUNTRY_NAME))
        }
    }

    private suspend fun getPreferences(key: String): String {
        val preferences = dataStore.data.first()
        return preferences[stringPreferencesKey(key)] ?: ""
    }
}

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(private val dataStore: DataStore<Preferences>) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(dataStore) as T
    }
}