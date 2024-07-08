package com.jdccmobile.domain.usecase

import com.jdccmobile.domain.model.City
import com.jdccmobile.domain.repository.PlaceRepository

class GetCitiesRemote(private val placeRepository: PlaceRepository) {
    suspend operator fun invoke(): List<City> = placeRepository.getCitiesListRemote()
}
