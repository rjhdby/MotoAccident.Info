package info.motoaccident.dictionaries

import com.google.gson.annotations.SerializedName

enum class AccidentDamage internal constructor(private val text: String) {
    @SerializedName("na") UNKNOWN("неизвестно"),
    @SerializedName("wo") NO("жив, цел, орёл!"),
    @SerializedName("l") LIGHT("вроде цел"),
    @SerializedName("h") HEAVY("вроде жив"),
    @SerializedName("d") LETHAL("летальный");

    override fun toString(): String {
        return this.text
    }
}
