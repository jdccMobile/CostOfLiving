package com.jdccmobile.domain.usecase

import com.jdccmobile.domain.model.City
import com.jdccmobile.domain.model.CityCost
import com.jdccmobile.domain.repository.PlaceRepository

class InsertCityCostLocaleUseCase(private val placeRepository: PlaceRepository) {
    suspend operator fun invoke(cityCost: CityCost) =
        placeRepository.insertCityCostLocal(cityCost)
}
