package com.jdccmobile.costofliving.data.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PlaceDb(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val cityName: String,
    val countryName: String,
)
