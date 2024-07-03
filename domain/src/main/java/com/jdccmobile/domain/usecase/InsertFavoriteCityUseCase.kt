package com.jdccmobile.domain.usecase

import com.jdccmobile.domain.model.Place
import com.jdccmobile.domain.repository.PlaceRepository

class InsertFavoriteCityUseCase(private val placeRepository: PlaceRepository) {
    suspend operator fun invoke(city: Place) =
        placeRepository.insertFavoriteCity(city)
}
