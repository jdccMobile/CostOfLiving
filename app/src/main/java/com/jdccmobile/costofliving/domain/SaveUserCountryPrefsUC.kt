package com.jdccmobile.costofliving.domain

import com.jdccmobile.costofliving.data.CostInfoRepository

class SaveUserCountryPrefsUC(private val costInfoRepository: CostInfoRepository) {
    suspend operator fun invoke(countryName: String) {
        costInfoRepository.saveUserCountryPrefs(countryName)
    }
}