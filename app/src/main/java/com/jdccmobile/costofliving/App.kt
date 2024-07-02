package com.jdccmobile.costofliving

import android.app.Application
import androidx.room.Room
import com.jdccmobile.data.database.FavoriteCitiesDatabase

class App : Application() {
    lateinit var db: FavoriteCitiesDatabase
        private set

    override fun onCreate() {
        super.onCreate()

        initDi()

        db = Room.databaseBuilder(
            this,
            FavoriteCitiesDatabase::class.java, "favorite_cities",
        ).build()
    }
}
