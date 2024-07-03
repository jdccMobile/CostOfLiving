package com.jdccmobile.domain.usecase

import com.jdccmobile.domain.model.Place
import com.jdccmobile.domain.repository.PlaceRepository

class GetFavoritePlacesUseCase(private val placeRepository: PlaceRepository) {
    suspend operator fun invoke(): List<Place> =
        placeRepository.getFavoritePlacesList()
}
