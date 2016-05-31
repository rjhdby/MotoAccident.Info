package info.motoaccident.utils

import android.location.Location
import android.text.format.DateFormat
import android.widget.EditText
import com.google.android.gms.maps.model.LatLng
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

fun LatLng.distance(flatLng: LatLng): Float {
    val results = FloatArray(3, { i -> 0f })
    Location.distanceBetween(this.latitude, this.longitude,
                             flatLng.latitude, flatLng.longitude,
                             results);
    return results[0]
}

fun String.md5(): String {
    try {
        val md = MessageDigest.getInstance("MD5")
        md.update(this.toByteArray())
        val digest = md.digest()
        val sb = StringBuilder()
        for (b in digest) {
            sb.append(String.format("%02x", b))
        }
        return sb.toString()
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
        return ""
    }
}

fun EditText.md5(): String = this.text.toString().md5()
fun EditText.value(): String = this.text.toString()
fun EditText.isNotEmpty(): Boolean = this.text.isNotEmpty()
fun Long.asTimeIntervalFromCurrent(): Long = System.currentTimeMillis() / 1000 - this
fun Long.asAge(): String {
    val diff = System.currentTimeMillis() / 1000 - this
    when {
        diff < 60 -> return diff.toString() + "с"
        diff < 3600 -> return (diff / 60).toString() + "м"
        diff < 14400 -> return (diff / 3600).toString() + "ч " + ((diff / 60) % 60).toString() + "м"
        diff < 86400 -> return DateFormat.format("HH:MM", Date(this * 1000)).toString()
        else -> return DateFormat.format("dd.MM.yy HH:MM", Date(this * 1000)).toString()
    }
}