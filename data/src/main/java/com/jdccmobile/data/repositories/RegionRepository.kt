package com.jdccmobile.data.repositories

import android.location.Geocoder
import android.location.Location
import com.jdccmobile.costofliving.data.LocationDataSource
import com.jdccmobile.costofliving.ui.common.PermissionChecker
import com.jdccmobile.costofliving.ui.features.main.MainActivity.Companion.DEFAULT_COUNTRY_CODE

class RegionRepository(
    private val locationDataSource: LocationDataSource,
    private val coarsePermissionChecker: PermissionChecker,
    private val geocoder: Geocoder,
) {
    suspend fun findLastRegion(): String = findLastLocation().toRegion()

    private suspend fun findLastLocation(): Location? {
        val success = coarsePermissionChecker.check()
        return if (success) locationDataSource.findLastLocation() else null
    }

    private fun Location?.toRegion(): String {
        val addresses = this?.let {
            geocoder.getFromLocation(latitude, longitude, 1)
        }
        return addresses?.firstOrNull()?.countryCode?.lowercase() ?: DEFAULT_COUNTRY_CODE
    }
}
