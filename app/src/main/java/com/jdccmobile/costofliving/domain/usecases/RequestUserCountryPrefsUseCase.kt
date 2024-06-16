package com.jdccmobile.costofliving.domain.usecases

import com.jdccmobile.costofliving.data.repositories.CostInfoRepository

class RequestUserCountryPrefsUseCase(private val costInfoRepository: CostInfoRepository) {
    suspend operator fun invoke(): String = costInfoRepository.requestUserCountryPrefs()
}
