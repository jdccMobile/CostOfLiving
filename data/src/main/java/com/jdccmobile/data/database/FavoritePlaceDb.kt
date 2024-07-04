package com.jdccmobile.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = FAVORITE_PLACES_TABLE)
data class FavoritePlaceDb(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "city_name") val cityName: String? = null,
    @ColumnInfo(name = "country_name") val countryName: String,
    @ColumnInfo(name = "is_favorite") val isFavorite: Boolean? = null,
)

const val FAVORITE_PLACES_TABLE = "favorite_places"
