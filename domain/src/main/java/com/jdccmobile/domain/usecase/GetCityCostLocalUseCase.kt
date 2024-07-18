package com.jdccmobile.domain.usecase

import com.jdccmobile.domain.model.CityCost
import com.jdccmobile.domain.repository.CityRepository

class GetCityCostLocalUseCase(private val cityRepository: CityRepository) {
    suspend operator fun invoke(cityId: Int): CityCost? =
        cityRepository.getCityCostLocal(cityId)
}
