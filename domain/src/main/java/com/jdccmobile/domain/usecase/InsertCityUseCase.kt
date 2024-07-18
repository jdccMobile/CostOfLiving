package com.jdccmobile.domain.usecase

import com.jdccmobile.domain.model.City
import com.jdccmobile.domain.repository.CityRepository

class InsertCityUseCase(private val cityRepository: CityRepository) {
    suspend operator fun invoke(city: City) =
        cityRepository.insertCity(city)
}
