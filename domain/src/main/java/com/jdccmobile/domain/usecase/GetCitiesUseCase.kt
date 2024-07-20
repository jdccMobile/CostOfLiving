package com.jdccmobile.domain.usecase

import arrow.core.Either
import com.jdccmobile.domain.model.City
import com.jdccmobile.domain.repository.CityRepository

class GetCitiesUseCase(private val cityRepository: CityRepository) {
    suspend operator fun invoke(): Either<Throwable, List<City>> =
        cityRepository.getCitiesListRemote()
}
