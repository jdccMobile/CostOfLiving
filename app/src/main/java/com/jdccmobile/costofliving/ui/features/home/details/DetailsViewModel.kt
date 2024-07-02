package com.jdccmobile.costofliving.ui.features.home.details

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdccmobile.costofliving.R
import com.jdccmobile.costofliving.common.ResourceProvider
import com.jdccmobile.costofliving.ui.models.ItemPriceUi
import com.jdccmobile.costofliving.ui.models.PlaceUi
import com.jdccmobile.costofliving.ui.models.toDomain
import com.jdccmobile.domain.model.ItemPrice
import com.jdccmobile.domain.usecase.CheckIsFavoriteCityUseCase
import com.jdccmobile.domain.usecase.DeleteFavoriteCityUseCase
import com.jdccmobile.domain.usecase.GetCityCostUseCase
import com.jdccmobile.domain.usecase.GetCountryCostUseCase
import com.jdccmobile.domain.usecase.InsertFavoriteCityUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val place: PlaceUi,
    private val getCityCostUseCase: GetCityCostUseCase,
    private val getCountryCostUseCase: GetCountryCostUseCase,
    private val resourceProvider: ResourceProvider,
    private val insertFavoriteCityUseCase: InsertFavoriteCityUseCase,
    private val deleteFavoriteCityUseCase: DeleteFavoriteCityUseCase,
    private val checkFavoriteCityUseCase: CheckIsFavoriteCityUseCase,

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
        when (place) {
            is PlaceUi.City -> {
                UiState(
                    cityName = place.cityName.replaceFirstChar { it.uppercase() },
                    countryName = place.countryName.replaceFirstChar { it.uppercase() },
                    isFavorite = place.isFavorite,
                )
            }

            is PlaceUi.Country -> {
                UiState(
                    cityName = null,
                    countryName = place.countryName.replaceFirstChar { it.uppercase() },
                    isFavorite = null, // TODO cambiar cuando se añada paises favoritos
                )
            }
        },
    )
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        Log.i("asd", "init" + _state.value.isFavorite)
        if (_state.value.isFavorite == null) {
            viewModelScope.launch {
                _state.value = _state.value.copy(
                    isFavorite = checkFavoriteCityUseCase(
                        PlaceUi.City(
                            countryName = _state.value.countryName,
                            cityName = _state.value.cityName ?: "", // TODO revisar
                        ).toDomain(),
                    ),
                )
            }
        }
        Log.i("asd", "init2" + _state.value.isFavorite)
        refresh()

        // TODO hacer peticion a la db buscando si esta la ciudad
//        if place isfavorite = null -> peticion a la bbdd si no es que viene desde favotiyos
    }

    private fun refresh() {
        viewModelScope.launch {
            createCostInfoList()
        }
    }

    private suspend fun createCostInfoList() {
        if (!_state.value.apiCallCompleted) {
            val pricesList: List<ItemPriceUi> = when (place) {
                is PlaceUi.City -> {
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

                is PlaceUi.Country -> {
                    try {
                        getCountryCostUseCase(countryName = place.countryName)
                    } catch (e: Exception) {
                        handleApiErrorMsg(e)
                        emptyList()
                    }
                }
            }.filter { // TODO improve this code
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
            onAddCityToFavoritesClicked()
//            binding.ivFavorite.setImageResource(R.drawable.ic_favorite_border)
            Toast.makeText(activity, "Favorito borrado", Toast.LENGTH_SHORT).show()
        } else {
            onDeleteCityFromFavoritesClicked()
//            binding.ivFavorite.setImageResource(R.drawable.ic_favorite)
            Toast.makeText(activity, "Favorito añadido", Toast.LENGTH_SHORT).show()
        }
    }


    fun onAddCityToFavoritesClicked() {
        // todo jdc cambiar nombre porque tambien sera el de borrar, para borrar añadir un dialog
        _state.value = _state.value.copy(isFavorite = true)
        viewModelScope.launch {
            if (place is PlaceUi.City) {
                insertFavoriteCityUseCase(place.toDomain())
            }
        }
    }

    fun onDeleteCityFromFavoritesClicked() {
        _state.value = _state.value.copy(isFavorite = false)
        // todo jdc cambiar nombre porque tambien sera el de borrar, para borrar añadir un dialog
        viewModelScope.launch {
            if (place is PlaceUi.City) {
                deleteFavoriteCityUseCase(place.toDomain())
            }
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
