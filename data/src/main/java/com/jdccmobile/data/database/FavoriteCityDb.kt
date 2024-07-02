package com.jdccmobile.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_cities")
data class FavoriteCityDb(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "city_name") val cityName: String,
    @ColumnInfo(name = "country_name") val countryName: String,
    @ColumnInfo(name = "is_favorite") val isFavorite: Boolean? = null,
)
