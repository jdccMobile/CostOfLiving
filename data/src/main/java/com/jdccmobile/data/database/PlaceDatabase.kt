package com.jdccmobile.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jdccmobile.data.database.citydb.CityDb
import com.jdccmobile.data.database.citydb.CityDao
import com.jdccmobile.data.database.countrydb.CountryDao
import com.jdccmobile.data.database.countrydb.CountryDb

@Database(
    entities = [CityDb::class, CountryDb::class],
    version = 1,
    exportSchema = false,
)
abstract class PlaceDatabase : RoomDatabase() {
    abstract fun getCityDao(): CityDao
    abstract fun getCountryDao(): CountryDao
}
