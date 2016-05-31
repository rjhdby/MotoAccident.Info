package info.motoaccident.network.modeles.list

import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName
import info.motoaccident.dictionaries.AccidentDamage
import info.motoaccident.dictionaries.AccidentStatus
import info.motoaccident.dictionaries.AccidentType
import java.util.*

class Point {
    val id: Int = 0
    val time: Long = 0
    @SerializedName("a") val address: String = ""
    @SerializedName("d") val description: String = ""
    @SerializedName("s") val status: AccidentStatus = AccidentStatus.ACTIVE
    @SerializedName("o") val owner: String = ""
    @SerializedName("oid") val ownerId: Int = 0
    val lat: Float = 55.752295f
    val lon: Float = 37.622735f
    @SerializedName("t") val type: AccidentType = AccidentType.OTHER
    val med: AccidentDamage = AccidentDamage.UNKNOWN
    @SerializedName("m") var messages: List<Message> = ArrayList()
    @SerializedName("v") var volunteers: List<Volunteer> = ArrayList()
    @SerializedName("h") var history: List<History> = ArrayList()

    fun location(): LatLng {
        return LatLng(lat.toDouble(), lon.toDouble())
    }

    fun isAccident(): Boolean {
        when (type) {
            AccidentType.MOTO_AUTO, AccidentType.MOTO_MAN, AccidentType.MOTO_MOTO, AccidentType.SOLO -> return true
            else -> return false
        }
    }
}
