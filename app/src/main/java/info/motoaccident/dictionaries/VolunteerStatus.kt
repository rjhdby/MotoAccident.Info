package info.motoaccident.dictionaries

import com.google.gson.annotations.SerializedName

enum class VolunteerStatus {
    @SerializedName("w") ON_WAY_STATUS,
    @SerializedName("l") LEAVE_STATUS,
    @SerializedName("i") IN_PLACE_STATUS,
    UNKNOWN
}