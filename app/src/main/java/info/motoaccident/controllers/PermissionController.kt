package info.motoaccident.controllers

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import info.motoaccident.dictionaries.*
import rx.Observable
import java.util.concurrent.TimeUnit

object PermissionController {
    var localPermissions = 0

    fun check(permission: Int): Boolean {
        buildLocalPermissions()
        return permission and localPermissions != 0
    }

    private fun buildLocalPermissions() {
        localPermissions = 0;
        when (UserController.role) {
            Role.READ_ONLY, Role.ANONYMOUS, Role.UNAUTHORIZED -> localPermissions = localPermissions or READONLY
            Role.STANDARD                                     -> localPermissions = localPermissions or STANDARD
            Role.MODERATOR                                    -> localPermissions = localPermissions or MODERATOR
            Role.DEVELOPER                                    -> localPermissions = localPermissions or DEVELOPER
        }
        if (phoneEnabled()) localPermissions = localPermissions or PHONE
        if (locationEnabled()) localPermissions = localPermissions or LOCATION
        if (notificationsEnabled()) localPermissions = localPermissions or NOTIFICATIONS
    }

    private fun phoneEnabled(): Boolean {
        //TODO implement
        return true
    }

    private fun locationEnabled(): Boolean {
        //TODO implement
        return true
    }

    private fun notificationsEnabled(): Boolean {
        //TODO implement
        return true
    }

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
