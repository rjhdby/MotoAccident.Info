package info.motoaccident.network.modeles.list

import com.google.gson.annotations.SerializedName
import info.motoaccident.dictionaries.VolunteerStatus

class Volunteer {
    @SerializedName("o") var owner: String = ""
    var oid: Int = 0
    @SerializedName("t") var timeStamp: Long = 0
    @SerializedName("s") var status: VolunteerStatus = VolunteerStatus.UNKNOWN
}
