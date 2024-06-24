package com.jdccmobile.domain.repository

interface CostInfoRepository {
    suspend fun requestUserCountryPrefs(): String

    suspend fun saveUserCountryPrefs(countryName: String): Unit

    suspend fun requestCitiesListRemote(): List<Any>

    suspend fun requestCityCostRemote(cityName: String, countryName: String): List<Any>

    suspend fun requestCountryCostRemote(countryName: String): List<Any>
}