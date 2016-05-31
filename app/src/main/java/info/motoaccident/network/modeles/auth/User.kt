package info.motoaccident.network.modeles.auth

import com.google.gson.annotations.SerializedName
import info.motoaccident.dictionaries.Role

class User {
    @SerializedName("r") var role: Role = Role.UNAUTHORIZED;
    var id: Int = 0;
}