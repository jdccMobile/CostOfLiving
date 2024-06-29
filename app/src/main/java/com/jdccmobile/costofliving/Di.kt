package com.jdccmobile.costofliving

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.jdccmobile.costofliving.common.PermissionCheckerImpl
import com.jdccmobile.costofliving.common.PlayServicesLocationDataSourceImpl
import com.jdccmobile.costofliving.common.ResourceProvider
import com.jdccmobile.costofliving.ui.features.home.details.DetailsViewModel
import com.jdccmobile.costofliving.ui.features.home.search.SearchViewModel
import com.jdccmobile.costofliving.ui.features.intro.IntroSlidesProvider
import com.jdccmobile.costofliving.ui.features.main.MainViewModel
import com.jdccmobile.costofliving.ui.models.PlaceUi
import com.jdccmobile.data.database.datasources.PlaceLocalDataSource
import com.jdccmobile.data.location.LocationDataSource
import com.jdccmobile.data.location.PermissionChecker
import com.jdccmobile.data.preferences.PreferencesDataSource
import com.jdccmobile.data.remote.datasources.PlaceRemoteDataSource
import com.jdccmobile.data.repositories.PlaceRepositoryImpl
import com.jdccmobile.data.repositories.PrefsRepositoryImpl
import com.jdccmobile.data.repositories.RegionRepository
import com.jdccmobile.domain.repository.PlaceRepository
import com.jdccmobile.domain.repository.PrefsRepository
import com.jdccmobile.domain.usecase.GetCityCostUseCase
import com.jdccmobile.domain.usecase.GetCityListUseCase
import com.jdccmobile.domain.usecase.GetCountryCostUseCase
import com.jdccmobile.domain.usecase.GetUserCountryPrefsUseCase
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val PREFERENCES = "preferences"
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

    single { ResourceProvider(get()) }
    single { IntroSlidesProvider(get()) }

    factory<LocationDataSource> { PlayServicesLocationDataSourceImpl(get()) }
    factory<PermissionChecker> { PermissionCheckerImpl(get()) }

    viewModel { MainViewModel(get()) }
    viewModel { SearchViewModel(get(), get(), get()) }
    viewModel { (place: PlaceUi) -> DetailsViewModel(place, get(), get(), get()) }
}

// TODO add room
private val dataModule = module {
    factory<PreferencesDataSource> { PreferencesDataSource(get()) }
    factory<PrefsRepository> { PrefsRepositoryImpl(get()) }

    factory<PlaceLocalDataSource> { PlaceLocalDataSource() }
    factory<PlaceRemoteDataSource> { PlaceRemoteDataSource(get(named("apiKey"))) }
    factory<PlaceRepository> { PlaceRepositoryImpl(get(), get()) }

    factory<RegionRepository> { RegionRepository(get(), get()) }
}

private val domainModule = module {
    factory { GetUserCountryPrefsUseCase(get()) }
    factory { GetCityListUseCase(get()) }
    factory { GetCityCostUseCase(get()) }
    factory { GetCountryCostUseCase(get()) }
}

private const val API_KEY_NAMED = "apiKey"
