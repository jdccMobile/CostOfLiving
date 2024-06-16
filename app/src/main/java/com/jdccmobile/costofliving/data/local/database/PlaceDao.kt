package com.jdccmobile.costofliving.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaceDao {
    @Query("SELECT * FROM PlaceDb")
    fun getAll(): Flow<List<PlaceDb>>

    @Query("SELECT * FROM PlaceDb WHERE id = :id")
    fun findById(id: Int): Flow<PlaceDb>

    @Query("SELECT COUNT(id) FROM PlaceDb")
    fun placeCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaces(places: List<PlaceDb>)
}
