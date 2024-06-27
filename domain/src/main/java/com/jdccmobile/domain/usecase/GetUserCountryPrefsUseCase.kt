package com.jdccmobile.domain.usecase

import com.jdccmobile.domain.repository.PrefsRepository

class GetUserCountryPrefsUseCase(private val prefsRepository: PrefsRepository) {
    suspend operator fun invoke(): String = prefsRepository.getUserCountry()
}
