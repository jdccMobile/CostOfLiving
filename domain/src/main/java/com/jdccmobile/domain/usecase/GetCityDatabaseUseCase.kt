package com.jdccmobile.domain.usecase

import com.jdccmobile.domain.model.City
import com.jdccmobile.domain.repository.PlaceRepository

class GetCityDatabaseUseCase(private val placeRepository: PlaceRepository) {
    suspend operator fun invoke(cityId: Int) =
        placeRepository.getCity(cityId)
}
