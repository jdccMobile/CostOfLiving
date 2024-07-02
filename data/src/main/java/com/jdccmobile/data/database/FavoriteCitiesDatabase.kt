package com.jdccmobile.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavoriteCityDb::class], version = 1, exportSchema = false)
abstract class FavoriteCitiesDatabase : RoomDatabase() {
    abstract fun getFavoriteCityDao(): FavoriteCityDao
}
