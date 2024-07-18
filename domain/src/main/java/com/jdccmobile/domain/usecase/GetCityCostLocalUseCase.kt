package com.jdccmobile.domain.usecase

import com.jdccmobile.domain.model.CityCost
import com.jdccmobile.domain.repository.PlaceRepository

class GetCityCostLocalUseCase(private val placeRepository: PlaceRepository) {
    suspend operator fun invoke(cityId: Int): CityCost? =
        placeRepository.getCityCostLocal(cityId)
}
