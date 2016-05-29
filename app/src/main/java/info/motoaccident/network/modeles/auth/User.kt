package info.motoaccident.network.modeles.auth

import com.google.gson.annotations.SerializedName
import info.motoaccident.dictionaries.Role

class User {
    @SerializedName("r") val role: Role = Role.UNAUTHORIZED;
    val id: Int = 0;
}