package com.jdccmobile.domain.usecase

import arrow.core.Either
import arrow.core.continuations.either
import com.jdccmobile.domain.model.CityCost
import com.jdccmobile.domain.model.ErrorType
import com.jdccmobile.domain.repository.CityRepository

class GetCityCostUseCase(
    private val cityRepository: CityRepository,
) {

    suspend operator fun invoke(
        cityId: Int,
        cityName: String,
        countryName: String,
    ): Either<ErrorType, CityCost> = either {
        cityRepository.getCityCostLocal(cityId).bind() ?: cityRepository.getCityCostRemote(
            cityName,
            countryName
        ).bind()
    }.mapLeft {
        ErrorType.NO_COINCIDENCES
    }

}
