package com.jdccmobile.domain.usecase

import com.jdccmobile.domain.model.City
import com.jdccmobile.domain.repository.PlaceRepository

class GetCitiesFromUserCountryUseCase(private val placeRepository: PlaceRepository) {
    suspend operator fun invoke(countryName: String): List<City> =
        placeRepository.getCitiesFromUserCountry(countryName)
}
