@file:Suppress("ktlint:standard:max-line-length")

package com.jdccmobile.data.database.citydb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface CityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(city: CityDb)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCitiesFromUserCountry(city: List<CityDb>)

    @Query("SELECT * FROM cities_table WHERE country_name = :countryName ORDER BY city_name ASC")
    suspend fun getCitiesFromUserCountry(countryName: String): List<CityDb>

    @Query("SELECT * FROM cities_table ORDER BY city_name ASC")
    suspend fun getCityList(): List<CityDb>

    @Query("SELECT * FROM cities_table WHERE is_favorite = true ORDER BY city_name ASC")
    suspend fun getFavoriteCitiesList(): List<CityDb>

    @Update
    suspend fun updateCity(city: CityDb)
}
