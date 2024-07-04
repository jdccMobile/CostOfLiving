package com.jdccmobile.domain.usecase

import com.jdccmobile.domain.model.Place
import com.jdccmobile.domain.repository.PlaceRepository

class CheckIsFavoritePlaceUseCase(private val placeRepository: PlaceRepository) {
    suspend operator fun invoke(
        city: Place.City,
    ): Boolean = placeRepository.checkIsFavoritePlace(city)
}
