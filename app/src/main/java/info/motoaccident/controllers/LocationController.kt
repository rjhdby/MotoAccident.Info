package info.motoaccident.controllers

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.util.Log
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.model.LatLng
import info.motoaccident.MyApplication
import info.motoaccident.R
import pl.charmas.android.reactivelocation.ReactiveLocationProvider
import rx.Observable
import rx.Subscription
import rx.subjects.PublishSubject

object LocationController : ActivityCompat.OnRequestPermissionsResultCallback {
    val locationUpdated: PublishSubject<LatLng> = PublishSubject.create()
    val locationEnabled: PublishSubject<Boolean> = PublishSubject.create()

    private val locationAdapter: ReactiveLocationProvider = ReactiveLocationProvider(MyApplication.context)
    private var locationSubscription: Subscription = Observable.empty<Boolean>().subscribe()

    var lastLocation = PreferencesController.lastLocation

    fun start() {
        MyApplication.onForeground.subscribe { s -> changeAccuracy(s) }
    }

    private fun changeAccuracy(fine: Boolean) {
        if (isEnabled()) {
            startUpdate(fine)
        } else {
            locationEnabled.subscribe { b -> if (b) startUpdate(fine) else locationUpdated.onNext(PreferencesController.lastLocation) }
        }
    }

    private fun startUpdate(fine: Boolean) {
        locationSubscription.unsubscribe()
        val request = LocationRequest.create()
                .setPriority(if (fine) LocationRequest.PRIORITY_HIGH_ACCURACY else LocationRequest.PRIORITY_LOW_POWER)
                .setSmallestDisplacement(if (fine) 100f else 2000f)
                .setInterval(if (fine) 5000 else 600000)
        locationSubscription = locationAdapter.getUpdatedLocation(request)
                .subscribe { location -> LocationController.locationUpdated.onNext(LatLng(location.latitude, location.longitude));lastLocation = LatLng(location.latitude, location.longitude) }
        Log.d("STATUS LOCATION", fine.toString())
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED) locationEnabled.onNext(true)
        else locationEnabled.onNext(false)
    }

    fun isEnabled(): Boolean {
        return android.os.Build.VERSION.SDK_INT < 23 || return MyApplication.context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission(context: Context) {
        if (isEnabled()) locationEnabled.onNext(true)
        else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, Manifest.permission.READ_CONTACTS)) {
                Snackbar.make(context.findViewById(R.id.coordinatorLayout), "В случае запрета определения местоположения будет установлено местоположение по умолчанию - центр г.Москва. В этом случае не забудьте поправить настройки радиуса оповещения.", Snackbar.LENGTH_LONG)
                        .setAction("ОК", { v -> ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), 1) })
                        .show();
            } else {
                ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.ACCESS_FINE_LOCATION), 1)
            }
        }
    }
}