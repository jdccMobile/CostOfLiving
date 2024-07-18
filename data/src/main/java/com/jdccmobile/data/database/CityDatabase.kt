package com.jdccmobile.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jdccmobile.data.database.citydb.CityDao
import com.jdccmobile.data.database.citydb.CityDb
import com.jdccmobile.data.database.costlifedb.CityCostDao
import com.jdccmobile.data.database.costlifedb.CityCostDb

@Database(
    entities = [CityDb::class, CityCostDb::class],
    version = 1,
    exportSchema = false,
)
abstract class CityDatabase : RoomDatabase() {
    abstract fun getCityDao(): CityDao

    abstract fun getCountryDao(): CityCostDao
}
