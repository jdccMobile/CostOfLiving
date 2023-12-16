package com.jdccmobile.costofliving.data

import android.Manifest
import android.annotation.SuppressLint
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.LocationServices
import com.jdccmobile.costofliving.ui.common.PermissionChecker
import com.jdccmobile.costofliving.ui.main.MainActivity.Companion.DEFAULT_COUNTRY_CODE
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class RegionRepository(activity: AppCompatActivity) {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
    private val coarsePermissionChecker = PermissionChecker(activity, Manifest.permission.ACCESS_COARSE_LOCATION)

    private val geocoder = Geocoder(activity)

    suspend fun findLastRegion(): String = findLastLocation().toRegion()

    private suspend fun findLastLocation(): Location? {
        val success = coarsePermissionChecker.request()
        return if (success) lastLocationSuspended() else null
    }

    @SuppressLint("MissingPermission")
    private suspend fun lastLocationSuspended(): Location? =
        suspendCancellableCoroutine { continuation ->
            fusedLocationClient.lastLocation.addOnCompleteListener {
                continuation.resume(it.result)
            }
        }

    private fun Location?.toRegion(): String {
        val addresses = this?.let {
            geocoder.getFromLocation(latitude, longitude, 1)
        }
        return addresses?.firstOrNull()?.countryCode?.lowercase() ?: DEFAULT_COUNTRY_CODE
    }
}