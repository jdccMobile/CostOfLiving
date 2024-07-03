package com.jdccmobile.domain.usecase

import com.jdccmobile.domain.model.Place
import com.jdccmobile.domain.repository.PlaceRepository

class InsertFavoriteCountryUseCase(private val placeRepository: PlaceRepository) {
    suspend operator fun invoke(country: Place.Country) =
        placeRepository.insertFavoriteCountry(country)
}
