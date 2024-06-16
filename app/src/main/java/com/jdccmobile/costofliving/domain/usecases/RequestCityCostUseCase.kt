package com.jdccmobile.costofliving.domain.usecases

import com.jdccmobile.costofliving.data.repositories.CostInfoRepository
import com.jdccmobile.costofliving.domain.models.ItemPrice

class RequestCityCostUseCase(private val costInfoRepository: CostInfoRepository) {
    suspend operator fun invoke(cityName: String, countryName: String): List<ItemPrice> =
        costInfoRepository.requestCityCost(cityName, countryName)
}
