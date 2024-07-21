package com.jdccmobile.domain.usecase

import arrow.core.Either
import arrow.core.continuations.either
import com.jdccmobile.domain.model.City
import com.jdccmobile.domain.model.ErrorType
import com.jdccmobile.domain.repository.CityRepository

class GetCitiesUseCase(private val cityRepository: CityRepository) {
    suspend operator fun invoke(): Either<Throwable, List<City>> = either {
        cityRepository.getCitiesListRemote().bind()
    }

}
