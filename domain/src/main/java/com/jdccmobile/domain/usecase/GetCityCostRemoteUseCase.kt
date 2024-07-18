package com.jdccmobile.domain.usecase

import com.jdccmobile.domain.model.CityCost
import com.jdccmobile.domain.repository.CityRepository

class GetCityCostRemoteUseCase(
    private val cityRepository: CityRepository,
) {
    suspend operator fun invoke(cityName: String, countryName: String): CityCost {
        cityRepository
        return cityRepository.getCityCostRemote(cityName, countryName)
    }
}
