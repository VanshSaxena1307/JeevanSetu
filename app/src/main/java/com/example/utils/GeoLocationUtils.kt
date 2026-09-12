package com.example.utils

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

object GeoLocationUtils {
    private const val EARTH_RADIUS_KM = 6371.0

    /**
     * Calculates the great-circle distance between two points using the Haversine formula.
     * Returns distance in kilometers.
     */
    fun calculateDistanceKm(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Double {
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val rLat1 = Math.toRadians(lat1)
        val rLat2 = Math.toRadians(lat2)

        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(rLat1) * cos(rLat2) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return EARTH_RADIUS_KM * c
    }

    /**
     * Calculates the initial bearing (compass heading) from point 1 to point 2 in degrees (0..360).
     */
    fun calculateBearing(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Float {
        val dLon = Math.toRadians(lon2 - lon1)
        val rLat1 = Math.toRadians(lat1)
        val rLat2 = Math.toRadians(lat2)

        val y = sin(dLon) * cos(rLat2)
        val x = cos(rLat1) * sin(rLat2) - sin(rLat1) * cos(rLat2) * cos(dLon)
        var brng = Math.toDegrees(atan2(y, x)).toFloat()
        brng = (brng + 360f) % 360f
        return brng
    }

    /**
     * Converts bearing degrees to standard 16-point cardinal compass directions.
     */
    fun bearingToCardinal(degrees: Float): String {
        val normalized = ((degrees % 360f) + 360f) % 360f
        val directions = arrayOf(
            "North", "North-Northeast", "North-East", "East-Northeast",
            "East", "East-Southeast", "South-East", "South-Southeast",
            "South", "South-Southwest", "South-West", "West-Southwest",
            "West", "West-Northwest", "North-West", "North-Northwest"
        )
        val index = ((normalized + 11.25f) / 22.5f).toInt() % 16
        return directions[index]
    }

    fun formatDistance(distanceKm: Double): String {
        return if (distanceKm < 1.0) {
            val meters = (distanceKm * 1000).toInt()
            "$meters meters"
        } else {
            String.format("%.1f km", distanceKm)
        }
    }
}
