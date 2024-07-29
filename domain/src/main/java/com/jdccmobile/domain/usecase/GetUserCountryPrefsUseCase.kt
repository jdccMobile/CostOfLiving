package com.jdccmobile.domain.usecase

import arrow.core.Either
import arrow.core.continuations.either
import com.jdccmobile.domain.repository.PrefsRepository

class GetUserCountryPrefsUseCase(private val prefsRepository: PrefsRepository) {
    suspend operator fun invoke(): Either<Throwable, String> = either {
        prefsRepository.getUserCountry().bind()
    }
}
