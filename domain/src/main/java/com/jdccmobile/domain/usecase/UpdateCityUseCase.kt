package com.jdccmobile.domain.usecase

import com.jdccmobile.domain.model.Place
import com.jdccmobile.domain.repository.PlaceRepository

class UpdateCityUseCase(private val placeRepository: PlaceRepository) {
    suspend operator fun invoke(city: Place.City) = placeRepository.updateCity(city)
}
