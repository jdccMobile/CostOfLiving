package com.jdccmobile.costofliving.ui.features.home.details

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdccmobile.costofliving.R
import com.jdccmobile.costofliving.common.ResourceProvider
import com.jdccmobile.costofliving.ui.models.CityUi
import com.jdccmobile.costofliving.ui.models.ItemPriceUi
import com.jdccmobile.costofliving.ui.utils.toDomain
import com.jdccmobile.domain.model.ItemPrice
import com.jdccmobile.domain.usecase.GetCityCostUseCase
import com.jdccmobile.domain.usecase.GetCountryCostUseCase
import com.jdccmobile.domain.usecase.InsertCityUseCase
import com.jdccmobile.domain.usecase.UpdateCityUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val city: CityUi,
    private val getCityCostUseCase: GetCityCostUseCase,
    private val getCountryCostUseCase: GetCountryCostUseCase,
    private val resourceProvider: ResourceProvider,
    private val insertCityUseCase: InsertCityUseCase,
    private val updateCityUseCase: UpdateCityUseCase,
) : ViewModel() {
    data class UiState(
        val cityName: String? = null,
        val countryName: String,
        val apiCallCompleted: Boolean = false,
        val itemCostInfoList: List<ItemPriceUi> = emptyList(),
        val isFavorite: Boolean?,
        val apiErrorMsg: String? = null,
    )

    private val _state = MutableStateFlow(
//        when (place) {
//            is PlaceUi.City -> {
        UiState(
            cityName = city.cityName.replaceFirstChar { it.uppercase() },
            countryName = city.countryName.replaceFirstChar { it.uppercase() },
            isFavorite = city.isFavorite,
        ),
//            }
//
//            is PlaceUi.Country -> {
//                UiState(
//                    cityName = null,
//                    countryName = place.countryName.replaceFirstChar { it.uppercase() },
//                    isFavorite = place.isFavorite,
//                )
//            }
//        },
    )
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
//        Log.i("asd", "init" + _state.value.isFavorite)
//        if (_state.value.isFavorite == null) {
//            viewModelScope.launch {
//                _state.value = _state.value.copy(
//                    isFavorite = checkFavoritePlaceUseCase(
//                        City(
//                            cityId = 1,// todo asd
//                            countryName = _state.value.countryName,
//                            // TODO revisar
//                            cityName = _state.value.cityName ?: "",
//                        ).toDomain(),
//                    ),
//                )
//            }
//        }
        refresh()
    }

    private fun refresh() {
        viewModelScope.launch {
            createCostInfoList()
        }
    }

    @Suppress("TooGenericExceptionCaught")
    private suspend fun createCostInfoList() {
        if (!_state.value.apiCallCompleted) {
            val pricesList: List<ItemPriceUi> =
                try {
                    getCityCostUseCase(
                        cityName = city.cityName,
                        countryName = city.countryName,
                    )
                } catch (e: Exception) {
                    handleApiErrorMsg(e)
                    emptyList()
                }
                    .filter { // TODO improve this code
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
                _state.value.copy(apiErrorMsg = resourceProvider.getString(R.string.http_429))
        } else {
            _state.value =
                _state.value.copy(
                    apiErrorMsg = resourceProvider.getString(R.string.connection_error),
                )
        }
    }

    fun onFavoriteClick(activity: FragmentActivity?) {
        if (_state.value.isFavorite == true) {
            onDeleteCityFromFavoritesClicked()
            Toast.makeText(activity, "Favorito borrado", Toast.LENGTH_SHORT).show()
        } else {
            onAddCityToFavoritesClicked()
            Toast.makeText(activity, "Favorito añadido", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onAddCityToFavoritesClicked() {
        viewModelScope.launch {
            insertCityUseCase(
                city.copy(isFavorite = !city.isFavorite).toDomain(),
            ) // TODO revisar
//                is PlaceUi.Country -> {
//                }
        }
//            _state.value = _state.value.copy(isFavorite = true)
    }

    private fun onDeleteCityFromFavoritesClicked() {
        viewModelScope.launch {
            updateCityUseCase(city.copy(isFavorite = !city.isFavorite).toDomain())
        }
    }
}

// TODO improve this code
private fun List<ItemPrice>.toUi(): List<ItemPriceUi> = map {
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
