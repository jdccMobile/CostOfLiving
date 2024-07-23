package com.jdccmobile.domain.usecase

import arrow.core.Either
import arrow.core.continuations.either
import com.jdccmobile.domain.model.CityCost
import com.jdccmobile.domain.repository.CityRepository

class InsertCityCostUseCase(private val cityRepository: CityRepository) {
    suspend operator fun invoke(cityCost: CityCost): Either<Throwable, Unit> = either {
        cityRepository.insertCityCostLocal(cityCost)
    }
}
