package com.jdccmobile.domain.repository

import arrow.core.Either

interface PrefsRepository {
    suspend fun setUserCountry(countryName: String): Either<Throwable, Unit>

    suspend fun getUserCountry(): Either<Throwable, String>
}
