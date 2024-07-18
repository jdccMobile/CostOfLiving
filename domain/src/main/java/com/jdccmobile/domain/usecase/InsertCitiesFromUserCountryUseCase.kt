package com.jdccmobile.domain.usecase

import com.jdccmobile.domain.model.City
import com.jdccmobile.domain.repository.CityRepository

class InsertCitiesFromUserCountryUseCase(private val cityRepository: CityRepository) {
    suspend operator fun invoke(cities: List<City>) =
        cityRepository.insertCitiesFromUserCountry(cities)
}
