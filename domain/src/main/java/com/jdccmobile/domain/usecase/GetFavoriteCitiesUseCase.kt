package com.jdccmobile.domain.usecase

import com.jdccmobile.domain.model.City
import com.jdccmobile.domain.repository.CityRepository

class GetFavoriteCitiesUseCase(private val cityRepository: CityRepository) {
    suspend operator fun invoke(): List<City> =
        cityRepository.getFavoriteCities()
}
