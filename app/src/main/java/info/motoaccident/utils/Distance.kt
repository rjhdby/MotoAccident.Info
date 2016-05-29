package info.motoaccident.utils

import android.location.Location
import com.google.android.gms.maps.model.LatLng

object Distance {
    fun beetwen(first: LatLng, second: LatLng): Float {
        val results = floatArrayOf();
        Location.distanceBetween(first.latitude, first.longitude,
                                 second.latitude, second.longitude,
                                 results);
        return results[0]
    }
}