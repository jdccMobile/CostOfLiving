package com.jdccmobile.costofliving.domain

import com.jdccmobile.costofliving.data.CostInfoRepository
import com.jdccmobile.costofliving.data.remote.model.cost.Price

class RequestCityCostUC(private val costInfoRepository: CostInfoRepository) {

    suspend operator fun invoke(cityName: String, countryName: String): List<Price> =
        costInfoRepository.requestCityCost(cityName, countryName).prices

}