package info.motoaccident.network.modeles.auth

import com.google.gson.annotations.SerializedName

class AuthResultModel {
    @SerializedName("r") var result: User = User();
    @SerializedName("e") var error: String = ""
    @SerializedName("d") var errorDescription: String = ""
}
