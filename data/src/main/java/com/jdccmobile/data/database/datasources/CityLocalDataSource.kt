package com.jdccmobile.data.database.datasources

import arrow.core.Either
import arrow.core.Either.Companion.catch
import com.jdccmobile.data.database.citydb.CityDao
import com.jdccmobile.data.database.citydb.toDb
import com.jdccmobile.data.database.citydb.toDomain
import com.jdccmobile.domain.model.City

class CityLocalDataSource(private val cityDao: CityDao) {
    suspend fun insertCity(city: City): Unit =
        cityDao.insertCity(city.toDb())

    suspend fun insertCitiesFromUserCountry(cities: List<City>): Unit =
        cityDao.insertCitiesFromUserCountry(cities.toDb())

    suspend fun getCitiesFromUserCountry(countryName: String): Either<Throwable, List<City>> =
        catch { cityDao.getCitiesFromUserCountry(countryName).toDomain() }

    suspend fun getCity(cityId: Int): Either<Throwable, City> =
        catch { cityDao.getCity(cityId).toDomain() }

    suspend fun getFavoriteCities(): Either<Throwable, List<City>> =
        catch { cityDao.getFavoriteCities().toDomain() }

    suspend fun updateCity(city: City): Either<Throwable, Unit> =
       catch { cityDao.updateCity(city.toDb()) }
}
