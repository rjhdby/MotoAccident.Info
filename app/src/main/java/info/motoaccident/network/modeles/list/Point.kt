package info.motoaccident.network.modeles.list

import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName
import info.motoaccident.dictionaries.AccidentDamage
import info.motoaccident.dictionaries.AccidentStatus
import info.motoaccident.dictionaries.AccidentType
import java.util.*

class Point {
    var id: Int = 0
    var time: Long = 0
    @SerializedName("a") var address: String = ""
    @SerializedName("d") var description: String = ""
    @SerializedName("s") var status: AccidentStatus = AccidentStatus.ACTIVE
    @SerializedName("o") var owner: String = ""
    var oid: Int = 0
    var lat: Float = 55.752295f
    var lon: Float = 37.622735f
    @SerializedName("t") var type: AccidentType = AccidentType.OTHER
    var med: AccidentDamage = AccidentDamage.UNKNOWN
    @SerializedName("m") var messages: List<Message> = ArrayList()
    @SerializedName("v") var volunteers: List<Volunteer> = ArrayList()
    @SerializedName("h") var history: List<History> = ArrayList()

    fun location(): LatLng {
        return LatLng(lat.toDouble(), lon.toDouble())
    }
}
