package com.jdccmobile.domain.repository

interface PrefsRepository {
    suspend fun setUserCountry(countryName: String): Unit

    suspend fun getUserCountry(): String
}
