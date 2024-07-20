package com.jdccmobile.domain.usecase

import arrow.core.Either
import com.jdccmobile.domain.model.City
import com.jdccmobile.domain.repository.CityRepository

class GetCitiesFromUserCountryUseCase(private val cityRepository: CityRepository) {
    suspend operator fun invoke(countryName: String): Either<Throwable, List<City>> =
        cityRepository.getCitiesFromUserCountry(countryName)
}
