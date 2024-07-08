package com.jdccmobile.domain.usecase

import com.jdccmobile.domain.model.City
import com.jdccmobile.domain.repository.PlaceRepository

class InsertCityUseCase(private val placeRepository: PlaceRepository) {
    suspend operator fun invoke(city: City) =
        placeRepository.insertCity(city)
}
