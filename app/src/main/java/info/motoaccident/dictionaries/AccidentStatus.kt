package info.motoaccident.dictionaries

import com.google.gson.annotations.SerializedName

enum class AccidentStatus internal constructor(private val text: String) {
    @SerializedName("a") ACTIVE("Активный"),
    @SerializedName("e") ENDED("Отбой"),
    @SerializedName("h") HIDDEN("Скрыт"),
    @SerializedName("c") CONFLICT("Конфликт"),
    @SerializedName("d") DUPLICATE("Дубль");

    override fun toString(): String {
        return this.text
    }
}
