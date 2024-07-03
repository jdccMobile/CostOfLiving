@file:Suppress("ktlint:standard:max-line-length")

package com.jdccmobile.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoritePlaceDao {
    @Query(
        "SELECT * FROM favorite_places WHERE is_favorite = true ORDER BY country_name ASC, city_name ASC",
    )
    suspend fun getFavoritePlacesList(): List<FavoritePlaceDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoritePlace(city: FavoritePlaceDb)

    @Query("SELECT * FROM favorite_places WHERE city_name IS NOT NULL")
    suspend fun getFavoriteCitiesList(): List<FavoritePlaceDb>

    @Query(
        "DELETE FROM favorite_places WHERE city_name = :cityName AND country_name = :countryName",
    )
    suspend fun deleteFavoritePlace(cityName: String?, countryName: String)

    @Query(
        "SELECT COUNT(*) FROM favorite_places WHERE city_name = :cityName AND country_name = :countryName",
    )
    suspend fun countFavoritePlace(cityName: String?, countryName: String): Int
}
