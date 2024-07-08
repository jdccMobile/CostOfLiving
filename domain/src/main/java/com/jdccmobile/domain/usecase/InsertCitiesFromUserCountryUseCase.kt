package com.jdccmobile.domain.usecase

import com.jdccmobile.domain.model.Place
import com.jdccmobile.domain.repository.PlaceRepository

class InsertCitiesFromUserCountryUseCase(private val placeRepository: PlaceRepository) {
    suspend operator fun invoke(cities: List<Place.City>) =
        placeRepository.insertCitiesFromUserCountry(cities)
}
