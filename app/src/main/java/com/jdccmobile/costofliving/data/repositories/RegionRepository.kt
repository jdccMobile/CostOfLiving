package com.jdccmobile.costofliving.data.repositories

import android.Manifest
import android.location.Geocoder
import android.location.Location
import com.google.android.gms.location.LocationServices
import com.jdccmobile.costofliving.App
import com.jdccmobile.costofliving.data.LocationDataSource
import com.jdccmobile.costofliving.data.PlayServicesLocationDataSource
import com.jdccmobile.costofliving.ui.common.PermissionChecker
import com.jdccmobile.costofliving.ui.features.main.MainActivity.Companion.DEFAULT_COUNTRY_CODE

class RegionRepository(application: App) {
    private val locationDataSource: LocationDataSource = PlayServicesLocationDataSource(application)
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)
    private val coarsePermissionChecker = PermissionChecker(
        application,
        Manifest.permission.ACCESS_COARSE_LOCATION,
    )

    private val geocoder = Geocoder(application)

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
