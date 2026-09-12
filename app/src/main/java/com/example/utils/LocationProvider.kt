package com.example.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.math.abs

data class DeviceLocation(
    val latitude: Double,
    val longitude: Double,
    val accuracyMeters: Float = 10f,
    val isRealGps: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

data class LocationDisplay(
    val city: String = "Bengaluru",
    val localitySubtitle: String = "Neeladri Road, Electronic City"
)

suspend fun <T> Task<T>.awaitTask(): T? = suspendCancellableCoroutine { cont ->
    addOnSuccessListener { result ->
        if (cont.isActive) cont.resume(result)
    }
    addOnFailureListener { exception ->
        if (cont.isActive) cont.resumeWithException(exception)
    }
    addOnCanceledListener {
        if (cont.isActive) cont.cancel()
    }
}

class LocationProvider(private val context: Context) {
    private val fusedClient = LocationServices.getFusedLocationProviderClient(context)
    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(fallbackLat: Double, fallbackLng: Double): DeviceLocation {
        try {
            val hasGpsProvider = locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) == true ||
                    locationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER) == true

            if (hasGpsProvider) {
                // 1. Check last location from fused client
                val lastLocation = fusedClient.lastLocation.awaitTask()
                if (lastLocation != null) {
                    return DeviceLocation(
                        latitude = lastLocation.latitude,
                        longitude = lastLocation.longitude,
                        accuracyMeters = lastLocation.accuracy,
                        isRealGps = true,
                        timestamp = lastLocation.time
                    )
                }

                // 2. Direct check from system LocationManager
                val lmGps: Location? = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                val lmNet: Location? = locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                val bestLm = lmGps ?: lmNet
                if (bestLm != null) {
                    return DeviceLocation(
                        latitude = bestLm.latitude,
                        longitude = bestLm.longitude,
                        accuracyMeters = bestLm.accuracy,
                        isRealGps = true,
                        timestamp = bestLm.time
                    )
                }

                // 3. Request fresh single update with high accuracy priority
                val tokenSource = CancellationTokenSource()
                val current = fusedClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    tokenSource.token
                ).awaitTask()

                if (current != null) {
                    return DeviceLocation(
                        latitude = current.latitude,
                        longitude = current.longitude,
                        accuracyMeters = current.accuracy,
                        isRealGps = true,
                        timestamp = current.time
                    )
                }
            }
        } catch (_: Exception) {
            // Permission not granted or Play services location unavailable, fallback smoothly
        }

        // Return configured fallback/simulated location with clear flag
        return DeviceLocation(
            latitude = fallbackLat,
            longitude = fallbackLng,
            accuracyMeters = 20f,
            isRealGps = false,
            timestamp = System.currentTimeMillis()
        )
    }

    suspend fun identifyAreaName(latitude: Double, longitude: Double): String = withContext(Dispatchers.IO) {
        try {
            if (Geocoder.isPresent()) {
                val geocoder = Geocoder(context, Locale.getDefault())
                @Suppress("DEPRECATION")
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                val address = addresses?.firstOrNull()
                if (address != null) {
                    val locality = address.locality ?: address.subAdminArea ?: address.adminArea
                    val subLocality = address.subLocality
                    val thoroughfare = address.thoroughfare
                    val areaTitle = when {
                        !subLocality.isNullOrBlank() && !locality.isNullOrBlank() -> "$subLocality, $locality"
                        !locality.isNullOrBlank() -> locality
                        !thoroughfare.isNullOrBlank() -> thoroughfare
                        else -> address.featureName
                    }
                    if (!areaTitle.isNullOrBlank()) {
                        return@withContext areaTitle
                    }
                }
            }
        } catch (_: Exception) {
            // Geocoder service may not be running or device offline
        }

        // Precise geographic heuristics if offline or Geocoder unavailable
        return@withContext matchGeographicRegionName(latitude, longitude)
    }

    suspend fun getDisplayLocationInfo(latitude: Double, longitude: Double): LocationDisplay = withContext(Dispatchers.IO) {
        try {
            if (Geocoder.isPresent()) {
                val geocoder = Geocoder(context, Locale.getDefault())
                @Suppress("DEPRECATION")
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                val address = addresses?.firstOrNull()
                if (address != null) {
                    val city = address.locality ?: address.subAdminArea ?: address.adminArea ?: "Bengaluru"
                    val sub = listOfNotNull(address.subLocality, address.thoroughfare).joinToString(", ")
                    val subtitle = if (sub.isNotBlank()) sub else "Neeladri Road, Electronic City"
                    return@withContext LocationDisplay(city = city, localitySubtitle = subtitle)
                }
            }
        } catch (_: Exception) {}

        // Fallback heuristics
        return@withContext when {
            abs(latitude - 12.9716) < 0.8 && abs(longitude - 77.5946) < 0.8 ->
                LocationDisplay(city = "Bengaluru", localitySubtitle = "Neeladri Road, Electronic City")
            abs(latitude - 28.6139) < 0.8 && abs(longitude - 77.2090) < 0.8 ->
                LocationDisplay(city = "New Delhi", localitySubtitle = "Connaught Place, Central District")
            abs(latitude - 19.0760) < 0.8 && abs(longitude - 72.8777) < 0.8 ->
                LocationDisplay(city = "Mumbai", localitySubtitle = "Marine Drive, Nariman Point")
            else ->
                LocationDisplay(city = "Bengaluru", localitySubtitle = "Neeladri Road, Electronic City")
        }
    }

    private fun matchGeographicRegionName(lat: Double, lng: Double): String {
        return when {
            abs(lat - 28.6139) < 0.6 && abs(lng - 77.2090) < 0.6 -> "Metro Capital District (Delhi NCR Sector)"
            abs(lat - 13.0827) < 0.7 && abs(lng - 80.2707) < 0.7 -> "Coastal Valley & Coromandel Delta Sector"
            abs(lat - 30.3165) < 0.7 && abs(lng - 78.0322) < 0.7 -> "Northern Himalayan Foothills & River Sector"
            abs(lat - 19.0760) < 0.7 && abs(lng - 72.8777) < 0.7 -> "Mumbai Konkan & Coastal Watershed"
            abs(lat - 12.9716) < 0.7 && abs(lng - 77.5946) < 0.7 -> "Bengaluru Urban & Deccan Plateau"
            abs(lat - 22.5726) < 0.7 && abs(lng - 88.3639) < 0.7 -> "Kolkata & Hooghly Delta Sector"
            else -> "Local GPS Sector (${String.format(Locale.US, "%.3f°N, %.3f°E", lat, lng)})"
        }
    }
}

