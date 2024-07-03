package com.jdccmobile.domain.usecase

import com.jdccmobile.domain.model.Place
import com.jdccmobile.domain.repository.PlaceRepository

class DeleteFavoriteCountryUseCase(private val placeRepository: PlaceRepository) {
    suspend operator fun invoke(country: Place.Country) =
        placeRepository.deleteFavoriteCountry(country)
}
