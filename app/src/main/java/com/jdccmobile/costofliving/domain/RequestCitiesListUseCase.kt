package com.jdccmobile.costofliving.domain

import com.jdccmobile.costofliving.data.CostInfoRepository
import com.jdccmobile.costofliving.data.remote.model.citieslist.CitiesListResult

class RequestCitiesListUseCase(private val costInfoRepository: CostInfoRepository) {
    suspend operator fun invoke(): CitiesListResult = costInfoRepository.requestCitiesList()
}
