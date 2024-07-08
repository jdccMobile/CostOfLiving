package com.jdccmobile.data.database.countrydb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jdccmobile.domain.model.Country
import com.jdccmobile.domain.model.PlaceType

@Entity(tableName = COUNTRIES_TABLE)
data class CountryDb(
    @PrimaryKey val countryId: String,
    @ColumnInfo(name = "country_name") val countryName: String,
    @ColumnInfo(name = "cities_in_country") val citiesInCountry: Int? = null,
    @ColumnInfo(name = "is_favorite") val isFavorite: Boolean,
    @ColumnInfo(name = "place_type") val placeType: PlaceType,
)

const val COUNTRIES_TABLE = "countries_table"

fun List<Country>.toDb(citiesInCountry: Int) = this.map { country ->
    CountryDb(
        countryId = country.countryId,
        countryName = country.countryName,
        isFavorite = country.isFavorite,
        citiesInCountry = citiesInCountry,
        placeType = country.placeType,
    )
}

fun List<CountryDb>.toDomain(): List<Country> = map { country ->
    Country(
        countryId = country.countryId,
        countryName = country.countryName,
        isFavorite = country.isFavorite,
    )
}

fun Country.toDb() =
    CountryDb(
        countryId = countryId,
        countryName = countryName,
        isFavorite = isFavorite,
        placeType = placeType,
    )
