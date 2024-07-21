package com.jdccmobile.domain.usecase

import arrow.core.Either
import arrow.core.continuations.either
import com.jdccmobile.domain.model.CityCost
import com.jdccmobile.domain.model.ErrorType
import com.jdccmobile.domain.repository.CityRepository

class GetCityCostUseCase(
    private val cityRepository: CityRepository,
    private val insertCityCostUseCase: InsertCityCostUseCase,
) {
    suspend operator fun invoke(
        cityId: Int,
        cityName: String,
        countryName: String,
    ): Either<Throwable, CityCost> = either {
        val cityCost = cityRepository.getCityCostLocal(cityId).bind() ?: cityRepository.getCityCostRemote(
            cityName,
            countryName,
        ).bind()
        insertCityCostUseCase(cityCost).bind()
        cityCost
    }
}
