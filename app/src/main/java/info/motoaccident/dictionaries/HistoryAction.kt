package info.motoaccident.dictionaries

import com.google.gson.annotations.SerializedName

/**
 * Created by rjhdby on 27.05.16.
 */
enum class HistoryAction {
    @SerializedName("act") ACTIVE,
    @SerializedName("end") END,
    @SerializedName("hide") HIDE,
    @SerializedName("create") CREATE,
    @SerializedName("finish") FINISH,
    UNKNOWN
}