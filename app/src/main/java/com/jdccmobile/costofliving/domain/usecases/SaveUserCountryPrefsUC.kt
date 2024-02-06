package com.jdccmobile.costofliving.domain.usecases

import com.jdccmobile.costofliving.data.CostInfoRepository

class SaveUserCountryPrefsUC(private val costInfoRepository: CostInfoRepository) {
    suspend operator fun invoke(countryName: String) {
        costInfoRepository.saveUserCountryPrefs(countryName)
    }
}