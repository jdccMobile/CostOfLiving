package com.jdccmobile.costofliving

import android.app.Application
import android.content.Context
import android.location.Geocoder
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.jdccmobile.costofliving.common.PermissionCheckerImpl
import com.jdccmobile.costofliving.common.PlayServicesLocationDataSourceImpl
import com.jdccmobile.costofliving.common.ResourceProvider
import com.jdccmobile.costofliving.ui.features.home.details.DetailsViewModel
import com.jdccmobile.costofliving.ui.features.home.favorites.FavoritesViewModel
import com.jdccmobile.costofliving.ui.features.home.search.SearchViewModel
import com.jdccmobile.costofliving.ui.features.intro.IntroSlidesProvider
import com.jdccmobile.costofliving.ui.features.intro.IntroViewModel
import com.jdccmobile.costofliving.ui.features.main.MainViewModel
import com.jdccmobile.data.database.CityDatabase
import com.jdccmobile.data.database.datasources.CityLocalDataSource
import com.jdccmobile.data.database.datasources.CostLifeLocalDataSource
import com.jdccmobile.data.location.LocationDataSource
import com.jdccmobile.data.location.PermissionChecker
import com.jdccmobile.data.preferences.PreferencesDataSource
import com.jdccmobile.data.remote.RetrofitService
import com.jdccmobile.data.remote.RetrofitServiceFactory
import com.jdccmobile.data.remote.datasources.CityRemoteDataSource
import com.jdccmobile.data.repositories.CityRepositoryImpl
import com.jdccmobile.data.repositories.PrefsRepositoryImpl
import com.jdccmobile.data.repositories.RegionRepository
import com.jdccmobile.domain.repository.CityRepository
import com.jdccmobile.domain.repository.PrefsRepository
import com.jdccmobile.domain.usecase.GetCitiesFromUserCountryUseCase
import com.jdccmobile.domain.usecase.GetCitiesUseCase
import com.jdccmobile.domain.usecase.GetCityCostUseCase
import com.jdccmobile.domain.usecase.GetCityUseCase
import com.jdccmobile.domain.usecase.GetFavoriteCitiesUseCase
import com.jdccmobile.domain.usecase.GetUserCountryPrefsUseCase
import com.jdccmobile.domain.usecase.InsertCitiesFromUserCountryUseCase
import com.jdccmobile.domain.usecase.InsertCityCostUseCase
import com.jdccmobile.domain.usecase.InsertCityUseCase
import com.jdccmobile.domain.usecase.InsertUserCountryPrefsUseCase
import com.jdccmobile.domain.usecase.UpdateCityUseCase
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

private const val PREFERENCES = "preferences"
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES)

fun Application.initDi() { // si tenemos application no necesitamos pasarselo por argumentos
    startKoin {
        androidLogger(Level.ERROR)
        androidContext(this@initDi) // le pasamos el this del application
        modules(appModule, dataModule, domainModule)
    }
}

private val appModule = module {
    single(named(API_KEY_NAMED)) { androidApplication().getString(R.string.api_key) }
    single { androidContext().dataStore }
    single { Geocoder(androidContext()) }

    singleOf(::ResourceProvider)
    singleOf(::IntroSlidesProvider)

    factoryOf(::PlayServicesLocationDataSourceImpl) bind LocationDataSource::class
    factoryOf(::PermissionCheckerImpl) bind PermissionChecker::class

    viewModelOf(::MainViewModel)
    viewModelOf(::IntroViewModel)
    viewModelOf(::SearchViewModel)
    viewModelOf(::DetailsViewModel)
    viewModelOf(::FavoritesViewModel)
}

private val dataModule = module {
    factoryOf(::RegionRepository)

    factoryOf(::PreferencesDataSource)
    factoryOf(::PrefsRepositoryImpl) bind PrefsRepository::class
    factoryOf(::CityLocalDataSource)
    factoryOf(::CostLifeLocalDataSource)
    single<RetrofitService> { RetrofitServiceFactory.makeRetrofitService() }
    factory<CityRemoteDataSource> { CityRemoteDataSource(get(named("apiKey")), get()) }
    factoryOf(::CityRepositoryImpl) bind CityRepository::class

    single {
        Room.databaseBuilder(
            androidContext(),
            CityDatabase::class.java,
            DATABASE_NAME,
        ).build()
    }
    single { get<CityDatabase>().getCityDao() }
    single { get<CityDatabase>().getCountryDao() }
}

private val domainModule = module {
    factoryOf(::GetUserCountryPrefsUseCase)
    factoryOf(::GetCitiesUseCase)
    factoryOf(::InsertCityUseCase)
    factoryOf(::UpdateCityUseCase)
    factoryOf(::GetFavoriteCitiesUseCase)
    factoryOf(::InsertCitiesFromUserCountryUseCase)
    factoryOf(::GetCityUseCase)
    factoryOf(::GetCitiesFromUserCountryUseCase)
    factoryOf(::GetCityCostUseCase)
    factoryOf(::InsertCityCostUseCase)
    factoryOf(::InsertUserCountryPrefsUseCase)
}

private const val API_KEY_NAMED = "apiKey"
private const val DATABASE_NAME = "favorites_database"
