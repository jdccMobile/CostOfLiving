package com.jdccmobile.costofliving.ui.features.home.details

import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdccmobile.costofliving.R
import com.jdccmobile.costofliving.common.ResourceProvider
import com.jdccmobile.costofliving.common.toStringResource
import com.jdccmobile.costofliving.ui.models.ItemPriceUi
import com.jdccmobile.domain.model.City
import com.jdccmobile.domain.model.CityCost
import com.jdccmobile.domain.usecase.GetCityCostUseCase
import com.jdccmobile.domain.usecase.GetCityUseCase
import com.jdccmobile.domain.usecase.UpdateCityUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val cityId: Int,
    private val resourceProvider: ResourceProvider,
    private val updateCityUseCase: UpdateCityUseCase,
    private val getCityUseCase: GetCityUseCase,
    private val getCiyCostUseCase: GetCityCostUseCase,
) : ViewModel() {
    data class UiState(
        val cityId: Int,
        val cityName: String = "",
        val countryName: String = "",
        val apiCallCompleted: Boolean = false,
        val itemCostInfoList: List<ItemPriceUi> = emptyList(),
        val isFavorite: Boolean = false,
        val apiErrorMsg: String? = null,
    )

    private val _state = MutableStateFlow(UiState(cityId = cityId))
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        initUi()
    }

    private fun initUi() {
        viewModelScope.launch {
            getCityInfo()
            getCityCosts()
        }
    }

    private suspend fun getCityInfo() {
        getCityUseCase(cityId).fold(
            { error ->
                _state.value = _state.value.copy(
                    apiCallCompleted = true,
                    apiErrorMsg = resourceProvider.getString(error.toStringResource()),
                )
            },
            { city ->
                _state.value = _state.value.copy(
                    cityId = city.cityId,
                    cityName = city.cityName.replaceFirstChar { it.uppercase() },
                    countryName = city.countryName.replaceFirstChar { it.uppercase() },
                    isFavorite = city.isFavorite,
                )
            },
        )
    }

    @Suppress("TooGenericExceptionCaught")
    private suspend fun getCityCosts() {
        getCiyCostUseCase(
            cityId = _state.value.cityId,
            cityName = _state.value.cityName,
            countryName = _state.value.countryName,
        ).fold(
            { error ->
                _state.value = _state.value.copy(
                    apiCallCompleted = true,
                    apiErrorMsg = resourceProvider.getString(error.toStringResource()),
                )
            },
            { cityCost ->
                _state.value = _state.value.copy(
                    apiCallCompleted = true,
                    itemCostInfoList = cityCost.toUi(),
                )
            },
        )
    }

    fun onFavoriteClick(activity: FragmentActivity?) {
        if (_state.value.isFavorite) {
            onFavoriteClicked()
            Toast.makeText(
                activity,
                resourceProvider.getString(R.string.favorite_deleted),
                Toast.LENGTH_SHORT,
            ).show()
        } else {
            onFavoriteClicked()
            Toast.makeText(
                activity,
                resourceProvider.getString(R.string.favorite_added),
                Toast.LENGTH_SHORT,
            ).show()
        }
    }

    private fun onFavoriteClicked() {
        viewModelScope.launch {
            updateCityUseCase(
                City(
                    cityId = _state.value.cityId,
                    cityName = _state.value.cityName,
                    countryName = _state.value.countryName,
                    isFavorite = !_state.value.isFavorite,
                ),
            ).fold(
                { error ->
                    _state.value = _state.value.copy(
                        apiErrorMsg = resourceProvider.getString(error.toStringResource()),
                    )
                },
                {
                    _state.value = _state.value.copy(isFavorite = !_state.value.isFavorite)
                },
            )
        }
    }
}

private fun CityCost?.toUi(): List<ItemPriceUi> = if (this != null) {
    listOf(
        ItemPriceUi(
            name = "Price per square meter to Buy Apartment Outside of City Center",
            cost = housePrice,
            imageId = R.drawable.im_city_centre,
        ),
        ItemPriceUi(
            name = "Gasoline, 1 liter",
            cost = gasolinePrice,
            imageId = R.drawable.im_gasoline,
        ),
        ItemPriceUi(
            name = "Summer Dress in a Chain Store Like George, H&M, Zara, etc.",
            cost = dressPrice,
            imageId = R.drawable.im_dress,
        ),
        ItemPriceUi(
            name = "Fitness Club, Monthly Fee for 1 Adult",
            cost = gymPrice,
            imageId = R.drawable.im_fitness,
        ),
        ItemPriceUi(
            name = "Coca-Cola, 0.33 liter Bottle",
            cost = cocaColaPrice,
            imageId = R.drawable.im_coca_cola,
        ),
        ItemPriceUi(
            name = "McMeal at McDonalds or Alternative Combo Meal",
            cost = mcMealPrice,
            imageId = R.drawable.im_mc_meal,
        ),
    )
} else {
    emptyList()
}
