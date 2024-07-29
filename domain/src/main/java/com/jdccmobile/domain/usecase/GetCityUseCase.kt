package com.jdccmobile.domain.usecase

import arrow.core.Either
import arrow.core.continuations.either
import com.jdccmobile.domain.model.City
import com.jdccmobile.domain.repository.CityRepository

class GetCityUseCase(private val cityRepository: CityRepository) {
    suspend operator fun invoke(cityId: Int): Either<Throwable, City> = either {
        cityRepository.getCity(cityId).bind()
    }
}
