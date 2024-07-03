package com.jdccmobile.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoritePlaceDao {

    @Query("SELECT * FROM favorite_places WHERE is_favorite = true ORDER BY country_name ASC, city_name ASC")
    suspend fun getFavoritePlacesList(): List<FavoritePlaceDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteCity(city: FavoritePlaceDb)

    @Query("SELECT * FROM favorite_places WHERE city_name IS NOT NULL")
    suspend fun getFavoriteCitiesList(): List<FavoritePlaceDb>

    @Query("DELETE FROM favorite_places WHERE city_name = :cityName AND country_name = :countryName")
    suspend fun deleteFavoriteCity(cityName: String, countryName: String)

    @Query("SELECT COUNT(*) FROM favorite_places WHERE city_name = :cityName AND country_name = :countryName")
    suspend fun countFavoriteCity(cityName: String, countryName: String): Int


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteCountry(city: FavoritePlaceDb)

    @Query("SELECT * FROM favorite_places WHERE city_name IS NULL")
    suspend fun getFavoriteCountriesList(): List<FavoritePlaceDb>

    @Query("DELETE FROM favorite_places WHERE city_name IS NULL AND country_name = :countryName")
    suspend fun deleteFavoriteCountry(countryName: String)

    @Query("SELECT COUNT(*) FROM favorite_places WHERE city_name IS NULL AND country_name = :countryName")
    suspend fun countFavoriteCountry(countryName: String): Int
}
