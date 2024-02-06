package com.jdccmobile.costofliving.domain.usecases

import com.jdccmobile.costofliving.data.CostInfoRepository
import com.jdccmobile.costofliving.data.remote.model.cost.Price

class RequestCountryCostUC(private val costInfoRepository: CostInfoRepository) {

    suspend operator fun invoke(countryName: String): List<Price> =
        costInfoRepository.requestCountryCost(countryName).prices
}