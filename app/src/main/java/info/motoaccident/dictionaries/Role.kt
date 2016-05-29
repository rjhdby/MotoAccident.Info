package info.motoaccident.dictionaries

import com.google.gson.annotations.SerializedName

enum class Role {
    @SerializedName("r") READ_ONLY_ROLE,
    @SerializedName("s") STANDARD_ROLE,
    @SerializedName("m") MODERATOR_ROLE,
    @SerializedName("d") DEVELOPER_ROLE,
    UNAUTHORIZED,
    ANONYMOUS
}