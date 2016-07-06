package info.motoaccident.controllers

import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.android.gms.maps.model.LatLng
import info.motoaccident.MyApplication
import rx.subjects.PublishSubject
//TODO no disturb
object PreferencesController {
    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.context)

    //Flow control
    val preferencesUpdated: PublishSubject<Boolean> = PublishSubject.create()

    //Preferences
    var login: String
        get() = preferences.getString("login", "")
        set(value) = preferences.edit().putString("login", value).apply()

    var passHash: String
        get() = preferences.getString("passHash", "")
        set(value) = preferences.edit().putString("passHash", value).apply()

    var locationEnabled: Boolean
        get() = preferences.getBoolean("location", true)
        set(value) = preferences.edit().putBoolean("location", value).apply()

    var lastLocation: LatLng
        get() = LatLng(preferences.getFloat("lat", 55.752295f).toDouble(), preferences.getFloat("lon", 37.622735f).toDouble())
        set(value) = preferences.edit()
                .putFloat("lat", value.latitude.toFloat())
                .putFloat("lon", value.longitude.toFloat())
                .apply()

    var noDisturb: Boolean
        get() = preferences.getBoolean("noDisturb", false)
        set(value) = preferences.edit().putBoolean("noDisturb", value).apply()

    var anonymous: Boolean
        get() = preferences.getBoolean("anonymous", false)
        set(value) = preferences.edit().putBoolean("anonymous", value).apply()

    var showRadius: Int
        get() = preferences.getInt("showRadius", 20)
        set(value) = preferences.edit().putInt("showRadius", value).apply()

    var accidents: Boolean
        get() = preferences.getBoolean("accidents", true)
        set(value) = preferences.edit().putBoolean("accidents", value).apply()

    var breaks: Boolean
        get() = preferences.getBoolean("breaks", true)
        set(value) = preferences.edit().putBoolean("breaks", value).apply()

    var steals: Boolean
        get() = preferences.getBoolean("steals", true)
        set(value) = preferences.edit().putBoolean("steals", value).apply()

    var other: Boolean
        get() = preferences.getBoolean("other", true)
        set(value) = preferences.edit().putBoolean("other", value).apply()

    var ended: Boolean
        get() = preferences.getBoolean("ended", true)
        set(value) = preferences.edit().putBoolean("ended", value).apply()

    var heavy: Boolean
        get() = preferences.getBoolean("heavy", true)
        set(value) = preferences.edit().putBoolean("heavy", value).apply()

    var maxAge: Int
        get() = preferences.getInt("maxAge", 24)
        set(value) = preferences.edit().putInt("maxAge", value).apply()

}
