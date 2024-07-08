package com.jdccmobile.costofliving.ui.utils

import com.jdccmobile.costofliving.ui.models.CityUi
import com.jdccmobile.costofliving.ui.models.CountryUi
import com.jdccmobile.domain.model.City
import com.jdccmobile.domain.model.Country

fun CityUi.toDomain() =
    City(
        cityId = cityId,
        cityName = cityName,
        countryName = countryName,
        isFavorite = isFavorite,
    )

fun CountryUi.toDomain() =
    Country(
        countryId = countryId,
        countryName = countryName,
        isFavorite = isFavorite,
    )

fun List<City>.toCityUi() = map { city ->
    CityUi(
        cityId = city.cityId,
        cityName = city.cityName,
        countryName = city.countryName,
        isFavorite = city.isFavorite,
    )
}

fun List<Country>.toUi() = map { country ->
    CountryUi(
        countryId = country.countryId,
        countryName = country.countryName,
        isFavorite = country.isFavorite,
    )
}
