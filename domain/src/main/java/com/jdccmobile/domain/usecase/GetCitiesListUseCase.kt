package com.jdccmobile.domain.usecase

import com.jdccmobile.domain.model.Place
import com.jdccmobile.domain.repository.PlaceRepository

class GetCitiesListUseCase(private val placeRepository: PlaceRepository) {
    suspend operator fun invoke(): List<Place.City> = placeRepository.getCitiesListRemote()
}
