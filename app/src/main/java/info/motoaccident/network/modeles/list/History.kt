package info.motoaccident.network.modeles.list

import com.google.gson.annotations.SerializedName
import info.motoaccident.dictionaries.HistoryAction

class History {
    var id: Int = 0
    @SerializedName("o") var owner: String = ""
    var oid: Int = 0
    @SerializedName("t") var timeStamp: Long = 0
    @SerializedName("a") var action: HistoryAction = HistoryAction.UNKNOWN
}
