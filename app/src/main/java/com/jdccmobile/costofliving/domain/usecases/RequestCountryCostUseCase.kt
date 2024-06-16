package com.jdccmobile.costofliving.domain.usecases

import com.jdccmobile.costofliving.data.repositories.CostInfoRepository
import com.jdccmobile.costofliving.domain.models.ItemPrice

class RequestCountryCostUseCase(private val costInfoRepository: CostInfoRepository) {
    suspend operator fun invoke(countryName: String): List<ItemPrice> =
        costInfoRepository.requestCountryCost(countryName)
}
