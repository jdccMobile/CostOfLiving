@file:Suppress("ktlint:standard:max-line-length")

package com.jdccmobile.data.database.countrydb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface CountryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCountry(country: CountryDb)

    @Query("SELECT * FROM countries_table WHERE is_favorite = true ORDER BY country_name ASC")
    suspend fun getFavoriteCountries(): List<CountryDb>

    @Update
    suspend fun updateFavoriteCountry(country: CountryDb)

}
