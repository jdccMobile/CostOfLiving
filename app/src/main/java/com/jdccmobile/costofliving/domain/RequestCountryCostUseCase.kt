package com.jdccmobile.costofliving.domain

import com.jdccmobile.costofliving.data.CostInfoRepository

class RequestCountryCostUseCase(private val costInfoRepository: CostInfoRepository) {

    suspend operator fun invoke(countryName: String) =
        costInfoRepository.requestCountryCost(countryName).prices
}