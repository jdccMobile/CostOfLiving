package com.jdccmobile.costofliving.ui.home.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jdccmobile.costofliving.R
import com.jdccmobile.costofliving.data.CostInfoRepository
import com.jdccmobile.costofliving.model.ItemCostInfo
import com.jdccmobile.costofliving.model.Place
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val place: Place,
    private val costInfoRepository: CostInfoRepository
) : ViewModel() {

    data class UiState(
        val cityName: String,
        val countryName: String,
        val costInfoLoaded: Boolean = false,
        val itemCostInfoList: List<ItemCostInfo> = emptyList(),
        val isFavorite: Boolean? = null
    )

    private val _state = MutableStateFlow(
        UiState(
            cityName = place.cityName ?: "",
            countryName = place.countryName,
        )
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
        if (!_state.value.costInfoLoaded) {
            Log.i("JD Details VM", "API call")
            val pricesList = if (place.cityName != null) {
                costInfoRepository.requestCityCost(place.cityName, place.countryName).prices
            } else {
                costInfoRepository.requestCountryCost(place.countryName).prices
            }
            val itemsToSearch = arrayOf(
                "in City Center",
                "Gasoline",
                "Dress",
                "Fitness",
                "Coca-Cola",
                "McMeal"
            )
            val itemsImageId = intArrayOf(
                R.drawable.im_city_centre,
                R.drawable.im_gasoline,
                R.drawable.im_dress,
                R.drawable.im_fitness,
                R.drawable.im_coca_cola,
                R.drawable.im_mc_meal
            )
            val itemsCostInfo: MutableList<ItemCostInfo> = mutableListOf()
            for (price in pricesList) {
                for (item in itemsToSearch.indices) {
                    if (price.itemName.contains(itemsToSearch[item])) {
                        itemsCostInfo += ItemCostInfo(price.itemName, price.avg, itemsImageId[item])
                    }
                }
            }
            _state.value = _state.value.copy(costInfoLoaded = true, itemCostInfoList = itemsCostInfo)
            Log.i("JD deatils vm", itemsCostInfo.toString())
            Log.i("JD deatils vm", pricesList.toString())
        }
    }


    fun changeFavStatus() {}

}

@Suppress("UNCHECKED_CAST")
class DetailsViewModelFactory(
    private val place: Place,
    private val costInfoRepository: CostInfoRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailsViewModel(place, costInfoRepository) as T
    }
}