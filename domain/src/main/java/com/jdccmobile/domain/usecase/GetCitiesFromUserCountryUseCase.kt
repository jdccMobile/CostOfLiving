package com.jdccmobile.domain.usecase

import com.jdccmobile.domain.model.Place
import com.jdccmobile.domain.repository.PlaceRepository

class GetCitiesFromUserCountryUseCase(private val placeRepository: PlaceRepository) {
    suspend operator fun invoke(countryName: String): List<Place.City> =
        placeRepository.getCitiesFromUserCountry(countryName)
}
