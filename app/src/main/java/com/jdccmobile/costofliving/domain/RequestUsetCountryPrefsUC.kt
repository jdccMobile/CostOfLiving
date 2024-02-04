package com.jdccmobile.costofliving.domain

import com.jdccmobile.costofliving.data.CostInfoRepository

class RequestUserCountryPrefsUC(private val costInfoRepository: CostInfoRepository) {

    suspend operator fun invoke(): String = costInfoRepository.requestUserCountryPrefs()
}