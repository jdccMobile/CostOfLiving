package com.jdccmobile.domain.usecase

import arrow.core.Either
import arrow.core.continuations.either
import com.jdccmobile.domain.repository.PrefsRepository

class InsertUserCountryPrefsUseCase(private val prefsRepository: PrefsRepository) {
    suspend operator fun invoke(countryName: String): Either<Throwable, Unit> = either {
        prefsRepository.setUserCountry(countryName).bind()
    }
}
