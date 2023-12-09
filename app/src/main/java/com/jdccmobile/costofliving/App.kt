package com.jdccmobile.costofliving

import android.app.Application
import androidx.room.Room
import com.jdccmobile.costofliving.data.local.database.PlaceDatabase

class App: Application() {

    lateinit var db: PlaceDatabase
        private set

    override fun onCreate() {
        super.onCreate()

        db = Room.databaseBuilder(
            this,
            PlaceDatabase::class.java, "movie-db"
        ).build()
    }
}

