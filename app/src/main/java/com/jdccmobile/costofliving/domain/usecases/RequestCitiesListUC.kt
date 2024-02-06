package com.jdccmobile.costofliving.domain.usecases

import com.jdccmobile.costofliving.data.CostInfoRepository
import com.jdccmobile.costofliving.data.remote.model.citieslist.City

class RequestCitiesListUC(private val costInfoRepository: CostInfoRepository) {
    suspend operator fun invoke(): List<City> = costInfoRepository.requestCitiesList().cities
}