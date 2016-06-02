package info.motoaccident.dictionaries

import com.google.gson.annotations.SerializedName

enum class HistoryAction {
    @SerializedName("act") ACTIVE,
    @SerializedName("end") END,
    @SerializedName("hide") HIDE,
    @SerializedName("create") CREATE,
    @SerializedName("finish") FINISH,
    UNKNOWN
}