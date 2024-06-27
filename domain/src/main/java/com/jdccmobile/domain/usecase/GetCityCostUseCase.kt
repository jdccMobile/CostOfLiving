package com.jdccmobile.domain.usecase

import com.jdccmobile.domain.model.ItemPrice
import com.jdccmobile.domain.repository.PlaceRepository

class GetCityCostUseCase(private val placeRepository: PlaceRepository) {
    suspend operator fun invoke(cityName: String, countryName: String): List<ItemPrice> =
        placeRepository.getCityCostRemote(cityName, countryName)
}
