package com.jdccmobile.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [com.jdccmobile.data.database.FavoritePlaceDb::class],
    version = 1,
    exportSchema = false,
)
abstract class FavoritePlacesDatabase : RoomDatabase() {
    abstract fun getFavoriteCityDao(): com.jdccmobile.data.database.FavoritePlaceDao
}
