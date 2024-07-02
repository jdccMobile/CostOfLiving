package com.jdccmobile.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_cities")
data class FavoriteCityDb(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "name") val cityName: String,
    @ColumnInfo(name = "country_name") val countryName: String,
)
