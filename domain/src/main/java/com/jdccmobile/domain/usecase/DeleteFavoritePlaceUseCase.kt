package com.jdccmobile.domain.usecase

import com.jdccmobile.domain.model.Place
import com.jdccmobile.domain.repository.PlaceRepository

class DeleteFavoritePlaceUseCase(private val placeRepository: PlaceRepository) {
    suspend operator fun invoke(city: Place) = placeRepository.deleteFavoritePlace(city)
}
