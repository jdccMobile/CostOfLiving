package com.jdccmobile.domain.usecase

import arrow.core.Either
import arrow.core.continuations.either
import com.jdccmobile.domain.model.City
import com.jdccmobile.domain.repository.CityRepository

class InsertCityUseCase(private val cityRepository: CityRepository) {
    suspend operator fun invoke(city: City): Either<Throwable, Unit> = either {
        cityRepository.insertCity(city)
    }
}
