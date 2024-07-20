package com.jdccmobile.costofliving.ui.features.home.details

import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdccmobile.costofliving.R
import com.jdccmobile.costofliving.common.ResourceProvider
import com.jdccmobile.costofliving.ui.models.ItemPriceUi
import com.jdccmobile.domain.model.City
import com.jdccmobile.domain.model.CityCost
import com.jdccmobile.domain.model.ErrorType
import com.jdccmobile.domain.usecase.GetCityCostUseCase
import com.jdccmobile.domain.usecase.GetCityLocalUseCase
import com.jdccmobile.domain.usecase.InsertCityCostUseCase
import com.jdccmobile.domain.usecase.UpdateCityUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val cityId: Int,
//    private val getCityCostRemoteUseCase: GetCityCostRemoteUseCase,
//    private val getCountryCostUseCase: GetCountryCostUseCase,
    private val resourceProvider: ResourceProvider,
//    private val insertCityUseCase: InsertCityUseCase,
    private val updateCityUseCase: UpdateCityUseCase,
    private val getCityLocalUseCase: GetCityLocalUseCase,
    private val getCiyCostLocalUseCase: GetCityCostUseCase,
    private val insertCityCostUseCase: InsertCityCostUseCase,
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
        val city = getCityLocalUseCase(cityId)
        _state.value = _state.value.copy(
            cityId = city.cityId,
            cityName = city.cityName.replaceFirstChar { it.uppercase() },
            countryName = city.countryName.replaceFirstChar { it.uppercase() },
            isFavorite = city.isFavorite,
        )
    }
    // TODO renombrar usecase y eliminar el usacese remote
    @Suppress("TooGenericExceptionCaught")
    private suspend fun getCityCosts() {
        getCiyCostLocalUseCase(
            cityId = _state.value.cityId,
            cityName = _state.value.cityName,
            countryName = _state.value.countryName
        ).fold(
            { errorType ->
                val errorMessage = when (errorType) {
                    ErrorType.HTTP_429 -> resourceProvider.getString(R.string.http_429)
                    ErrorType.CONNECTION -> resourceProvider.getString(R.string.connection_error)
                    ErrorType.NO_COINCIDENCES -> "" // TODO sacar a funcion comun
                }
                _state.value = _state.value.copy(
                    apiCallCompleted = true,
                    apiErrorMsg = errorMessage,
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
            Toast.makeText(activity, "Favorito borrado", Toast.LENGTH_SHORT).show()
        } else {
            onFavoriteClicked()
            Toast.makeText(activity, "Favorito añadido", Toast.LENGTH_SHORT).show()
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
            )
            _state.value = _state.value.copy(isFavorite = !_state.value.isFavorite)
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
