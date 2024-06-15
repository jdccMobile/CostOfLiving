package com.jdccmobile.costofliving.domain

import com.jdccmobile.costofliving.data.CostInfoRepository

class RequestUserCountryPrefsUseCase(private val costInfoRepository: CostInfoRepository) {
    suspend operator fun invoke(): String = costInfoRepository.requestUserCountryPrefs()
}
