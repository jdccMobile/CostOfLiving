package com.jdccmobile.costofliving.ui.features.home.details

import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jdccmobile.costofliving.R
import com.jdccmobile.costofliving.ui.models.ItemPriceUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val fragment: FragmentActivity,
    private val place: com.jdccmobile.domain.model.Place,
    private val getCityCostUseCase: com.jdccmobile.domain.usecase.GetCityCostUseCase,
    private val getCountryCostUseCase: com.jdccmobile.domain.usecase.GetCountryCostUseCase,
) : ViewModel() {
    data class UiState(
        val cityName: String? = null,
        val countryName: String,
        val apiCallCompleted: Boolean = false,
        val itemCostInfoList: List<ItemPriceUi> = emptyList(),
        val isFavorite: Boolean? = null,
        val apiErrorMsg: String? = null,
    )

    private val _state = MutableStateFlow(
        when (place) {
            is com.jdccmobile.domain.model.Place.City -> {
                UiState(
                    cityName = place.cityName.replaceFirstChar { it.uppercase() },
                    countryName = place.countryName.replaceFirstChar { it.uppercase() },
                )
            }

            is com.jdccmobile.domain.model.Place.Country -> {
                UiState(
                    cityName = null,
                    countryName = place.countryName.replaceFirstChar { it.uppercase() },
                )
            }
        },
    )
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        refresh()
    }

    private fun refresh() {
        viewModelScope.launch {
            createCostInfoList()
        }
    }

    private suspend fun createCostInfoList() {
        if (!_state.value.apiCallCompleted) {
            val pricesList: List<ItemPriceUi> = when (place) {
                is com.jdccmobile.domain.model.Place.City -> {
                    try {
                        getCityCostUseCase(
                            cityName = place.cityName,
                            countryName = place.countryName,
                        )
                    } catch (e: Exception) {
                        handleApiErrorMsg(e)
                        emptyList()
                    }
                }

                is com.jdccmobile.domain.model.Place.Country -> {
                    try {
                        getCountryCostUseCase(countryName = place.countryName)
                    } catch (e: Exception) {
                        handleApiErrorMsg(e)
                        emptyList()
                    }
                }
            }.filter {
                it.name.contains("in City Center") || it.name.contains("Gasoline") ||
                    it.name.contains("Dress") || it.name.contains("Fitness") ||
                    it.name.contains("Gasoline") || it.name.contains("McMeal") ||
                    it.name.contains("Coca-Cola")
            }.toUi()
            _state.value = _state.value.copy(
                apiCallCompleted = true,
                itemCostInfoList = pricesList,
            )
        }
    }

    private fun handleApiErrorMsg(e: Exception) {
        Log.e("JD Search VM", "API call requestCitiesList error: $e")
        if (e.message?.contains("429") == true) {
            _state.value =
                _state.value.copy(apiErrorMsg = fragment.getString(R.string.http_429))
        } else {
            _state.value =
                _state.value.copy(apiErrorMsg = fragment.getString(R.string.connection_error))
        }
    }

    fun changeFavStatus() {
        // TODO
    }
}

// TODO improve this code
private fun List<com.jdccmobile.domain.model.ItemPrice>.toUi(): List<ItemPriceUi> = map {
    ItemPriceUi(
        name = it.name,
        cost = it.cost,
        imageId = when {
            it.name.contains("in City Center") -> R.drawable.im_city_centre
            it.name.contains("Gasoline") -> R.drawable.im_gasoline
            it.name.contains("Dress") -> R.drawable.im_dress
            it.name.contains("Fitness") -> R.drawable.im_fitness
            it.name.contains("Coca-Cola") -> R.drawable.im_coca_cola
            else -> R.drawable.im_mc_meal
        },
    )
}

@Suppress("UNCHECKED_CAST")
class DetailsViewModelFactory(
    private val fragment: FragmentActivity,
    private val place: com.jdccmobile.domain.model.Place,
    private val getCityCostUseCase: com.jdccmobile.domain.usecase.GetCityCostUseCase,
    private val getCountryCostUseCase: com.jdccmobile.domain.usecase.GetCountryCostUseCase,
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailsViewModel(
            fragment,
            place,
            getCityCostUseCase,
            getCountryCostUseCase,
        ) as T
    }
}