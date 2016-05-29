package info.motoaccident.dictionaries

import com.google.gson.annotations.SerializedName

enum class Role {
    @SerializedName("r") READ_ONLY,
    @SerializedName("s") STANDARD,
    @SerializedName("m") MODERATOR,
    @SerializedName("d") DEVELOPER,
    UNAUTHORIZED,
    ANONYMOUS
}