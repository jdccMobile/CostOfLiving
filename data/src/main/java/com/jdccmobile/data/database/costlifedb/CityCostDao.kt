@file:Suppress("ktlint:standard:max-line-length")

package com.jdccmobile.data.database.costlifedb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CityCostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCityCost(cityCost: CityCostDb)

    @Query("SELECT * FROM cost_life_table WHERE cityId = :cityId")
    suspend fun getCityCostLocal(cityId: Int): CityCostDb?

//    @Update
//    suspend fun updateFavoriteCountry(country: CostLifeDb)
}
