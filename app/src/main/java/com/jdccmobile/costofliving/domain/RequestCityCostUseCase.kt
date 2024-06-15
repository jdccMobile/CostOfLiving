package com.jdccmobile.costofliving.domain

import com.jdccmobile.costofliving.data.CostInfoRepository

class RequestCityCostUseCase(private val costInfoRepository: CostInfoRepository) {
    suspend operator fun invoke(cityName: String, countryName: String) =
        costInfoRepository.requestCityCost(cityName, countryName).prices
}
