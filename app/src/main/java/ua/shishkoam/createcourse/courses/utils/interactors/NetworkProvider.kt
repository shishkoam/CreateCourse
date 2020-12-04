package ua.kblogika.interactive.utils.interactors

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Handler
import android.provider.Settings
import ua.shishkoam.createcourse.R

object NetworkProvider {
    private const val AIRPLANE_DIALOG_DELAY_MILLIS = 10000 // 10sec
    fun isNetworkConnected(activity: Activity): Boolean {
        val cm = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val ni = cm.activeNetworkInfo
        return ni != null
    }

    fun isGpsEnabled(activity: Activity): Boolean {
        val service = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return service.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    /**
     * Check airplane mode
     */
}