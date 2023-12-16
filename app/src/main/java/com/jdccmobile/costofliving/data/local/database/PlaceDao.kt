package com.jdccmobile.costofliving.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaceDao {

    @Query("SELECT * FROM Place")
    fun getAll(): Flow<List<Place>>

    @Query("SELECT * FROM Place WHERE id = :id")
    fun findById(id:Int): Flow<Place>

    @Query("SELECT COUNT(id) FROM Place")
    fun placeCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaces(places : List<Place>)
}