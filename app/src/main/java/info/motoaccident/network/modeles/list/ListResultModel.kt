package info.motoaccident.network.modeles.list

import com.google.gson.annotations.SerializedName
import java.util.*

class ListResultModel {
    @SerializedName("r") var result: List<Point> = ArrayList()
    @SerializedName("e") var error: String = ""
    @SerializedName("d") var errorDescription: String = ""
}
