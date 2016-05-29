package info.motoaccident.controllers

import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.android.gms.maps.model.LatLng
import info.motoaccident.MyApplication

object PreferencesController {
    private lateinit var preferences: SharedPreferences

    init {
        preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.context)
    }

    var login: String = preferences.getString("login", "");
        set(value) {
            preferences.edit().putString("login", value).apply()
        }
    var passHash: String = preferences.getString("passHash", "");
        set(value) {
            preferences.edit().putString("passHash", value).apply()
        }
    var lastLocation: LatLng = LatLng(preferences.getFloat("lat", 55.752295f).toDouble(), preferences.getFloat("lon", 37.622735f).toDouble())
        set(value) {
            preferences.edit()
                    .putFloat("lat", value.latitude.toFloat())
                    .putFloat("lon", value.longitude.toFloat())
                    .apply()
        }
//    var zoom: Float = preferences.getFloat("zoom", 10f)
//        set(value) {
//            preferences.edit().putFloat("zoom", value).apply()
//        }

    var anonymous: Boolean = preferences.getBoolean("anonymous", false);
        set(value) {
            preferences.edit().putBoolean("anonymous", value).apply()
        }

    var showRadius: Int = preferences.getInt("showRadius", 20)
        set(value) {
            preferences.edit().putInt("showRadius", value).apply()
        }
    var accidents: Boolean = preferences.getBoolean("accidents", true);
        set(value) {
            preferences.edit().putBoolean("accidents", value).apply()
        }
    var breaks: Boolean = preferences.getBoolean("breaks", true);
        set(value) {
            preferences.edit().putBoolean("breaks", value).apply()
        }
    var steals: Boolean = preferences.getBoolean("steals", true);
        set(value) {
            preferences.edit().putBoolean("steals", value).apply()
        }
    var other: Boolean = preferences.getBoolean("other", true);
        set(value) {
            preferences.edit().putBoolean("other", value).apply()
        }
    var ended: Boolean = preferences.getBoolean("ended", true);
        set(value) {
            preferences.edit().putBoolean("ended", value).apply()
        }
    var heavy: Boolean = preferences.getBoolean("heavy", true);
        set(value) {
            preferences.edit().putBoolean("heavy", value).apply()
        }
//    var camera: CameraPosition = CameraPosition(latLng, zoom, 0f, 0f)
//
//    fun updateCamera(cameraPosition: CameraPosition) {
//        preferences.edit().putFloat("lat", cameraPosition.target.latitude.toFloat())
//                .putFloat("lon", cameraPosition.target.longitude.toFloat())
//                .putFloat("zoom", cameraPosition.zoom)
//                .apply()
//    }
}
