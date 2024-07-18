package com.jdccmobile.costofliving.ui.utils

import com.jdccmobile.costofliving.ui.models.CityUi
import com.jdccmobile.domain.model.City

fun CityUi.toDomain() =
    City(
        cityId = cityId,
        cityName = cityName,
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

