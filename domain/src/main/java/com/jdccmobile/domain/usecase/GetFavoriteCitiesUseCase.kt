package com.jdccmobile.domain.usecase

import arrow.core.Either
import arrow.core.computations.ResultEffect.bind
import arrow.core.continuations.either
import com.jdccmobile.domain.model.City
import com.jdccmobile.domain.model.ErrorType
import com.jdccmobile.domain.repository.CityRepository

class GetFavoriteCitiesUseCase(private val cityRepository: CityRepository) {
    suspend operator fun invoke(): Either<ErrorType, List<City>> = either{
        cityRepository.getFavoriteCities().bind()
    }.mapLeft {
        ErrorType.NO_COINCIDENCES
    }
}
