package com.jdccmobile.costofliving

import android.app.Application
// import androidx.room.Room
// import com.jdccmobile.data.database.PlaceDatabase

class App : Application() {
//    lateinit var db: PlaceDatabase
//        private set

    override fun onCreate() {
        super.onCreate()

        initDi()
//
//        db = Room.databaseBuilder(
//            this,
//            PlaceDatabase::class.java, "movie-db",
//        ).build()
    }
}
