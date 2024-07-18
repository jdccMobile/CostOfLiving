package com.jdccmobile.data.database.datasources

import com.jdccmobile.data.database.costlifedb.CityCostDao
import com.jdccmobile.data.database.costlifedb.toDb
import com.jdccmobile.data.database.costlifedb.toDomain
import com.jdccmobile.domain.model.CityCost

class CostLifeLocalDataSource(private val cityCostDao: CityCostDao) {
    suspend fun insertCityCost(cityCost: CityCost): Unit =
        cityCostDao.insertCityCost(cityCost.toDb())

    suspend fun getCityCostLocal(cityId: Int): CityCost? =
        cityCostDao.getCityCostLocal(cityId)?.toDomain()
}
