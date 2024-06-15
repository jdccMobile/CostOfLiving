package com.jdccmobile.costofliving.data.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Place(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val cityName: String,
    val countryName: String,
)
