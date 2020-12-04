package ua.kblogika.interactive.utils.util

import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationManager
import android.util.Pair
import java.util.*

class GpsUtils {
    private fun getGpsInform(location: Location, locationManager: LocationManager?): String {

        //https://developer.android.com/reference/android/location/Location#getAccuracy%28%29
//        Location location = new Location(GPS_PROVIDER);
        val map: MutableMap<String, Number> = HashMap()
        val bmap: MutableMap<String, Boolean> = HashMap()
        if (locationManager != null) {
            val satData = calculateCurrentSatellitesNumber(locationManager)
            if (satData != null) {
                map["satellites"] = satData.first
                map["satellitesInFix"] = satData.second
            }
        }
        map["0000satellitesInFix"] = location.extras.getInt("satellites")
        map["location accuracy"] = location.accuracy
        bmap["location has a horizontal accuracy"] = location.hasAccuracy()
        //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            map.put("location getBearingAccuracyDegrees", location.getBearingAccuracyDegrees());
//            map.put("SpeedAccuracyMetersPerSecond", location.getSpeedAccuracyMetersPerSecond());
//            map.put("getVerticalAccuracyMeters (Altitude)", location.getVerticalAccuracyMeters());
//            bmap.put(" location has a bearing accuracy", location.hasBearingAccuracy());
//            bmap.put(" location has a speed accuracy", location.hasSpeedAccuracy());
//            bmap.put(" location has a vertical accuracy", location.hasVerticalAccuracy());
//        }
        val date = Date(location.time)
        val stringBuilder = StringBuilder()
        for ((key, number) in map) {
            stringBuilder.append("$key: $number\n")
        }
        for ((key, value) in bmap) {
            stringBuilder.append("$key: $value\n")
        }
        stringBuilder.append(date.toString())
        return stringBuilder.toString()
    }

    //https://stackoverflow.com/questions/6580603/getting-the-number-of-satellites-from-location-object
    @SuppressLint("MissingPermission")
    private fun calculateCurrentSatellitesNumber(locationManager: LocationManager): Pair<Int, Int> {
        var satellites = 0
        var satellitesInFix = 0
        for (sat in locationManager.getGpsStatus(null)!!.satellites) {
            if (sat.usedInFix()) {
                satellitesInFix++
            }
            satellites++
        }
        return Pair(satellites, satellitesInFix)
    }
}