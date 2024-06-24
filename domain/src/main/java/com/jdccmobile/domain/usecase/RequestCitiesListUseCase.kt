package com.jdccmobile.domain.usecase

import com.jdccmobile.costofliving.data.repositories.CostInfoRepository
import com.jdccmobile.domain.model.Place

class RequestCitiesListUseCase(private val costInfoRepository: CostInfoRepository) {
    suspend operator fun invoke(): List<Place.City> = costInfoRepository.requestCitiesList()
}
