package info.motoaccident.dictionaries

import com.google.gson.annotations.SerializedName

enum class AccidentType internal constructor(private val text: String) {
    @SerializedName("b") BREAK("Поломка"),
    @SerializedName("1") SOLO("Один участник"),
    @SerializedName("m") MOTO_MOTO("Мот/мот"),
    @SerializedName("a") MOTO_AUTO("Мот/авто"),
    @SerializedName("p") MOTO_MAN("Наезд на пешехода"),
    @SerializedName("o") OTHER("Прочее"),
    @SerializedName("s") STEAL("Угон");

    override fun toString(): String {
        return this.text
    }
}
