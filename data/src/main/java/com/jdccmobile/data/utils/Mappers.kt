package com.jdccmobile.data.utils

import com.jdccmobile.data.database.citydb.CityDb
import com.jdccmobile.data.database.countrydb.CountryDb
import com.jdccmobile.domain.model.Place

fun Place.City.toDb() =
    CityDb(
        cityId = cityId,
        cityName = cityName,
        countryName = countryName,
        isFavorite = isFavorite,
        placeType = placeType,
    )


fun Place.Country.toDb() =
    CountryDb(
        countryId = countryId,
        countryName = countryName,
        isFavorite = isFavorite,
        placeType = placeType,
    )

fun List<Place.City>.toDb() = this.map { city ->
    CityDb(
        cityId = city.cityId,
        cityName = city.cityName,
        countryName = city.countryName,
        isFavorite = city.isFavorite,
        placeType = city.placeType,
    )
}

fun List<Place.Country>.toDb(citiesInCountry: Int) = this.map { country ->
    CountryDb(
        countryId = country.countryId,
        countryName = country.countryName,
        isFavorite = country.isFavorite,
        citiesInCountry = citiesInCountry,
        placeType = country.placeType,
    )
}

fun List<CityDb>.toCityDomain(): List<Place.City> = map { city ->
        Place.City(
            cityId = city.cityId,
            cityName = city.cityName,
            countryName = city.countryName,
            isFavorite = city.isFavorite,
        )
}

fun List<CountryDb>.toCountryDomain(): List<Place.Country> = map { country ->
        Place.Country(
            countryId = country.countryId,
            countryName = country.countryName,
            isFavorite = country.isFavorite,
        )
    }

