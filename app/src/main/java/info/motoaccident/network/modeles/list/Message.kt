package info.motoaccident.network.modeles.list

import com.google.gson.annotations.SerializedName

open class Message {
    val id: Int = 0
    @SerializedName("o") val owner: String = ""
    @SerializedName("oid") val ownerId: Int = 0
    @SerializedName("t") val timeStamp: Long = 0
    @SerializedName("d") val text: String = ""
}
