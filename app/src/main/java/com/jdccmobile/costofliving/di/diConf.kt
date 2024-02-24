package com.jdccmobile.costofliving.di

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.jdccmobile.costofliving.App
import com.jdccmobile.costofliving.data.CostInfoRepository
import com.jdccmobile.costofliving.data.RegionRepository
import com.jdccmobile.costofliving.data.datasources.CostInfoLocalDataSource
import com.jdccmobile.costofliving.data.datasources.CostInfoRemoteDataSource
import com.jdccmobile.costofliving.data.datasources.PreferencesDataSource

import com.jdccmobile.costofliving.data.local.database.PlaceDatabase
import com.jdccmobile.costofliving.domain.model.Place
import com.jdccmobile.costofliving.domain.usecases.FindLastRegionUC
import com.jdccmobile.costofliving.domain.usecases.RequestCitiesListUC
import com.jdccmobile.costofliving.domain.usecases.RequestCityCostUC
import com.jdccmobile.costofliving.domain.usecases.RequestCountryCostUC
import com.jdccmobile.costofliving.domain.usecases.RequestUserCountryPrefsUC
import com.jdccmobile.costofliving.domain.usecases.SaveUserCountryPrefsUC
import com.jdccmobile.costofliving.ui.home.details.DetailsViewModel
import com.jdccmobile.costofliving.ui.home.search.SearchViewModel
import com.jdccmobile.costofliving.ui.intro.IntroViewModel
import com.jdccmobile.costofliving.ui.main.MainViewModel
import com.jdccmobile.costofliving.ui.main.PREFERENCES
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

fun Application.initDi() {
    startKoin {
        androidLogger(Level.ERROR)
        androidContext(this@initDi)
        modules(appModule, dataModule, useCasesModule)
    }
}

private val appModule = module {

    single {
        Room.databaseBuilder(
            get(),
            PlaceDatabase::class.java, "movie-db"
        ).build()
    }

    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.create(
            produceFile = { androidContext().dataStoreFile(PREFERENCES) }
        )
    }

    factory { PreferencesDataSource(get()) }
    factory { CostInfoRemoteDataSource(get()) }
    factory { CostInfoLocalDataSource() }

    factory { CostInfoRepository(get(), get(), get()) }
    factory { RegionRepository(get()) }

    viewModel { MainViewModel(get()) }
    viewModel { IntroViewModel(get(), get(), get()) }
    viewModel { SearchViewModel(get(), get(), get()) }
    viewModel { (place: Place) -> DetailsViewModel(get(), place, get(), get()) }
}

private val dataModule = module {

}

private val useCasesModule = module {
    factory { FindLastRegionUC(get()) }
    factory { RequestCitiesListUC(get()) }
    factory { RequestCityCostUC(get()) }
    factory { RequestCountryCostUC(get()) }
    factory { RequestUserCountryPrefsUC(get()) }
    factory { SaveUserCountryPrefsUC(get()) }
}