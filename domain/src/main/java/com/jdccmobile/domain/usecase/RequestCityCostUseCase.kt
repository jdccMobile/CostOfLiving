package com.jdccmobile.domain.usecase

import com.jdccmobile.costofliving.data.repositories.CostInfoRepository
import com.jdccmobile.domain.model.ItemPrice

class RequestCityCostUseCase(private val costInfoRepository: CostInfoRepository) {
    suspend operator fun invoke(cityName: String, countryName: String): List<ItemPrice> =
        costInfoRepository.requestCityCost(cityName, countryName)
}
