package com.jdccmobile.domain.usecase

import com.jdccmobile.domain.repository.CityRepository

class GetCityLocalUseCase(private val cityRepository: CityRepository) {
    suspend operator fun invoke(cityId: Int) =
        cityRepository.getCity(cityId)
}
