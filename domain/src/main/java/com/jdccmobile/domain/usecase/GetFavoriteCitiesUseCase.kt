package com.jdccmobile.domain.usecase

import com.jdccmobile.domain.model.Place

class GetFavoriteCitiesUseCase() {
    suspend operator fun invoke(): List<Place.City> = // TODO get data from room
        listOf(
            Place.City("Russia", "Moscow"),
            Place.City("Spain", "Madrid"),
        )
}
