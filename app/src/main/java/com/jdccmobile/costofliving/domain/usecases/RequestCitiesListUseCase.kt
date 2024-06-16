package com.jdccmobile.costofliving.domain.usecases

import com.jdccmobile.costofliving.data.repositories.CostInfoRepository
import com.jdccmobile.costofliving.domain.models.Place

class RequestCitiesListUseCase(private val costInfoRepository: CostInfoRepository) {
    suspend operator fun invoke(): List<Place.City> = costInfoRepository.requestCitiesList()
}
