package com.jdccmobile.domain.usecase

import com.jdccmobile.domain.model.Place
import com.jdccmobile.domain.repository.PlaceRepository

class CheckIsFavoriteCountryUseCase(private val placeRepository: PlaceRepository) {
    suspend operator fun invoke(country: Place.Country): Boolean =
        placeRepository.checkIsFavoriteCountry(country)
}
