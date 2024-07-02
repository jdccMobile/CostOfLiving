package com.jdccmobile.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteCityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteCity(city: FavoriteCityDb)

    @Query("SELECT * FROM favorite_cities")
    suspend fun getFavoriteCitiesList(): List<FavoriteCityDb>

    @Query("DELETE FROM favorite_cities WHERE id = :id")
    suspend fun deleteFavoriteCity(id: Int)
}
