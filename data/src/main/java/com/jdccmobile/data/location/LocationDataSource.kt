package com.jdccmobile.data.location

interface LocationDataSource {
    suspend fun findLastRegion(): String?
}
