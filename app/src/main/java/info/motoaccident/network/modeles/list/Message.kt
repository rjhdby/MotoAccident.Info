package info.motoaccident.network.modeles.list

import com.google.gson.annotations.SerializedName

class Message {
    var id: Int = 0
    @SerializedName("o") var owner: String = ""
    var oid: Int = 0
    @SerializedName("t") var timeStamp: Long = 0
    @SerializedName("d") var text: String = ""
}
