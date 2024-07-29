package com.jdccmobile.data.database.datasources

import arrow.core.Either
import arrow.core.Either.Companion.catch
import com.jdccmobile.data.database.costlifedb.CityCostDao
import com.jdccmobile.data.database.costlifedb.toDb
import com.jdccmobile.data.database.costlifedb.toDomain
import com.jdccmobile.domain.model.CityCost

class CostLifeLocalDataSource(private val cityCostDao: CityCostDao) {
    suspend fun insertCityCost(cityCost: CityCost): Either<Throwable, Unit> =
        catch { cityCostDao.insertCityCost(cityCost.toDb()) }

    suspend fun getCityCostLocal(cityId: Int): Either<Throwable, CityCost?> =
        catch { cityCostDao.getCityCostLocal(cityId)?.toDomain() }
}
