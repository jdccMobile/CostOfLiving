package com.jdccmobile.domain.usecase

import arrow.core.Either
import arrow.core.continuations.either
import com.jdccmobile.domain.model.City
import com.jdccmobile.domain.model.ErrorType
import com.jdccmobile.domain.repository.CityRepository

class GetCitiesFromUserCountryUseCase(
    private val cityRepository: CityRepository,
    private val getCitiesUseCase: GetCitiesUseCase,
    private val insertCitiesFromUserCountryUseCase: InsertCitiesFromUserCountryUseCase,
) {
    suspend operator fun invoke(userCountryName: String): Either<ErrorType, List<City>> = either {
        val citiesInUserCountry = cityRepository.getCitiesFromUserCountry(userCountryName).bind()

        if (citiesInUserCountry.isEmpty() || citiesInUserCountry.size != citiesInUserCountry[0].citiesInCountry) {
            val cities = getCitiesUseCase().bind()
            val filteredCitiesByCountry = cities.filter { it.countryName == userCountryName }
                .sortedBy { it.cityName }
            insertCitiesFromUserCountryUseCase(filteredCitiesByCountry)
            filteredCitiesByCountry
        } else {
            citiesInUserCountry
        }
    }.mapLeft { error ->
        when {
            error.message?.contains("429") == true -> ErrorType.HTTP_429
            else -> ErrorType.CONNECTION
        }
    }
}
