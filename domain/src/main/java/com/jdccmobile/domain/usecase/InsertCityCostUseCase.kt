package com.jdccmobile.domain.usecase

import com.jdccmobile.domain.model.CityCost
import com.jdccmobile.domain.repository.CityRepository

class InsertCityCostUseCase(private val cityRepository: CityRepository) {
    suspend operator fun invoke(cityCost: CityCost) =
        cityRepository.insertCityCostLocal(cityCost)
}
