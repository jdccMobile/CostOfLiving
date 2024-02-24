package com.jdccmobile.costofliving

import android.app.Application
import com.jdccmobile.costofliving.di.initDi

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        initDi()
    }
}
