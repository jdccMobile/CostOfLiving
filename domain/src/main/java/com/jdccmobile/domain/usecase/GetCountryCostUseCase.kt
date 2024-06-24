package com.jdccmobile.domain.usecase

import com.jdccmobile.domain.model.ItemPrice
import com.jdccmobile.domain.repository.PlaceRepository

class GetCountryCostUseCase(private val placeRepository: PlaceRepository) {
    suspend operator fun invoke(countryName: String): List<ItemPrice> =
        placeRepository.getCountryCostRemote(countryName)
}
