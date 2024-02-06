package com.jdccmobile.costofliving.ui.home.details

import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jdccmobile.costofliving.R
import com.jdccmobile.costofliving.data.remote.model.cost.Price
import com.jdccmobile.costofliving.domain.usecases.RequestCityCostUC
import com.jdccmobile.costofliving.domain.usecases.RequestCountryCostUC
import com.jdccmobile.costofliving.domain.model.ItemCostInfo
import com.jdccmobile.costofliving.domain.model.Place
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val fragment: FragmentActivity,
    private val place: Place,
    private val requestCityCostUC: RequestCityCostUC,
    private val requestCountryCostUC: RequestCountryCostUC
) : ViewModel() {

    data class UiState(
        val cityName: String? = null,
        val countryName: String,
        val apiCallCompleted: Boolean = false,
        val itemCostInfoList: List<ItemCostInfo> = emptyList(),
        val isFavorite: Boolean? = null,
        val apiErrorMsg: String? = null
    )

    private val _state = MutableStateFlow(
        UiState(
            cityName = place.cityName?.replaceFirstChar { it.uppercase() },
            countryName = place.countryName.replaceFirstChar { it.uppercase() }
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
        if (!_state.value.apiCallCompleted) {
            val pricesList: List<Price>
            if (place.cityName != null) {
                pricesList = try {
                    requestCityCostUC(place.cityName, place.countryName)
                } catch (e: Exception) {
                    handleApiErrorMsg(e)
                    emptyList()
                }
            } else {
                pricesList = try {
                    requestCountryCostUC(place.countryName)

                } catch (e: Exception) {
                    handleApiErrorMsg(e)
                    emptyList()
                }
            }
            _state.value = _state.value.copy(apiCallCompleted = true)

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
            _state.value = _state.value.copy(itemCostInfoList = itemsCostInfo)
        }
    }

    private fun handleApiErrorMsg(e: Exception) {
        Log.e("JD Search VM", "API call requestCitiesList error: $e")
        if(e.message?.contains("429") == true){
            _state.value = _state.value.copy(apiErrorMsg = fragment.getString(R.string.http_429))
        } else {
            _state.value = _state.value.copy(apiErrorMsg = fragment.getString(R.string.connection_error))
        }
    }


    fun changeFavStatus() {}



}


@Suppress("UNCHECKED_CAST")
class DetailsViewModelFactory(
    private val fragment: FragmentActivity,
    private val place: Place,
    private val requestCityCostUC: RequestCityCostUC,
    private val requestCountryCostUC: RequestCountryCostUC
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailsViewModel(fragment, place, requestCityCostUC, requestCountryCostUC) as T
    }
}