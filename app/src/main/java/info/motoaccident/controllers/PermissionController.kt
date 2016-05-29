package info.motoaccident.controllers

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import rx.Observable
import java.util.concurrent.TimeUnit

object PermissionController {
    private val LOCATION_PERMISSION = 1

    private var activity: Activity? = null
    var locationPermissionTrigger = Observable.just(activity != null && (isPermitted(ACCESS_FINE_LOCATION) || isPermitted(ACCESS_COARSE_LOCATION)))
            .repeatWhen { b -> b.delay(1, TimeUnit.SECONDS) }
            .filter { b -> b }.take(1)

    private fun locationPermission() {
        if (isPermitted(ACCESS_FINE_LOCATION) || isPermitted(ACCESS_COARSE_LOCATION)) return
        ActivityCompat.requestPermissions(activity!!,
                arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION),
                LOCATION_PERMISSION)
    }

    fun init(activity: Activity) {
        PermissionController.activity = activity
        if (android.os.Build.VERSION.SDK_INT < 23) return
        locationPermission()
    }

    private fun isPermitted(permission: String): Boolean {
        return android.os.Build.VERSION.SDK_INT < 23 || activity!!.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }
}
