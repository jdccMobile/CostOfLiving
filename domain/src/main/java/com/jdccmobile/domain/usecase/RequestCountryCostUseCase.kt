package com.jdccmobile.domain.usecase

import com.jdccmobile.costofliving.data.repositories.CostInfoRepository
import com.jdccmobile.domain.model.ItemPrice

class RequestCountryCostUseCase(private val costInfoRepository: CostInfoRepository) {
    suspend operator fun invoke(countryName: String): List<ItemPrice> =
        costInfoRepository.requestCountryCost(countryName)
}
