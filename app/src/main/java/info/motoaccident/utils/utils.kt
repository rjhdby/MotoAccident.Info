package info.motoaccident.utils

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.format.DateFormat
import android.view.View
import android.widget.EditText
import com.google.android.gms.maps.model.LatLng
import java.security.MessageDigest
import java.util.*

fun LatLng.distance(flatLng: LatLng): Float {
    val results = FloatArray(3, { i -> 0f })
    Location.distanceBetween(this.latitude, this.longitude,
                             flatLng.latitude, flatLng.longitude,
                             results);
    return results[0]
}

fun String.md5(): String {
    val md = MessageDigest.getInstance("MD5")
    md.update(this.toByteArray())
    val digest = md.digest()
    val sb = StringBuilder()
    for (b in digest) sb.append(String.format("%02x", b))
    return sb.toString()
}

fun EditText.md5(): String = this.text.toString().md5()
fun EditText.value(): String = this.text.toString()
fun EditText.isNotEmpty(): Boolean = this.text.isNotEmpty()
fun Long.asTimeIntervalFromCurrent(): Long = System.currentTimeMillis() / 1000 - this
fun Long.asAge(): String {
    val diff = System.currentTimeMillis() / 1000 - this
    when {
        diff < 60    -> return diff.toString() + "с"
        diff < 3600  -> return (diff / 60).toString() + "м"
        diff < 14400 -> return (diff / 3600).toString() + "ч " + ((diff / 60) % 60).toString() + "м"
        diff < 72000 -> return DateFormat.format("HH:mm", Date(this * 1000)).toString()
        else         -> return DateFormat.format("dd.MM HH:mm", Date(this * 1000)).toString()
    }
}

fun Float.asDistance(): String {
    if (this < 1000) return toInt().toString() + "м"
    else return Math.round(this / 1000).toString() + "км"
}

fun View.visible(visibility: Boolean) {
    this.visibility = if (visibility) View.VISIBLE else View.INVISIBLE
}

fun AppCompatActivity.runActivity(activity: Class<*>, bundle: Bundle = Bundle.EMPTY) {
    startActivity(Intent(this, activity).putExtras(bundle).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT))
}
