package com.jdccmobile.domain.usecase

import com.jdccmobile.domain.model.CityCost
import com.jdccmobile.domain.repository.PlaceRepository

class GetCityCostRemoteUseCase(
    private val placeRepository: PlaceRepository,
) {
    suspend operator fun invoke(cityName: String, countryName: String): CityCost {
        placeRepository
        return placeRepository.getCityCostRemote(cityName, countryName)
    }
}
