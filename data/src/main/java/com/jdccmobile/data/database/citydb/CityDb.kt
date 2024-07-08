package com.jdccmobile.data.database.citydb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jdccmobile.domain.model.City
import com.jdccmobile.domain.model.PlaceType

@Entity(tableName = CITIES_TABLE)
data class CityDb(
    @PrimaryKey val cityId: Int,
    @ColumnInfo(name = "city_name") val cityName: String,
    @ColumnInfo(name = "country_name") val countryName: String,
    @ColumnInfo(name = "is_favorite") val isFavorite: Boolean,
    @ColumnInfo(name = "place_type") val placeType: PlaceType,
)

const val CITIES_TABLE = "cities_table"

fun List<CityDb>.toDomain(): List<City> = map { city ->
    City(
        cityId = city.cityId,
        cityName = city.cityName,
        countryName = city.countryName,
        isFavorite = city.isFavorite,
    )
}

fun List<City>.toDb() = this.map { city ->
    CityDb(
        cityId = city.cityId,
        cityName = city.cityName,
        countryName = city.countryName,
        isFavorite = city.isFavorite,
        placeType = city.placeType,
    )
}

fun City.toDb() =
    CityDb(
        cityId = cityId,
        cityName = cityName,
        countryName = countryName,
        isFavorite = isFavorite,
        placeType = placeType,
    )
