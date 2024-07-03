package com.jdccmobile.domain.usecase

import com.jdccmobile.domain.model.Place
import com.jdccmobile.domain.repository.PlaceRepository

class GetFavoriteCountriesUseCase(private val placeRepository: PlaceRepository) {
    suspend operator fun invoke(): List<Place.Country> =
        placeRepository.getFavoriteCountriesList()
}
