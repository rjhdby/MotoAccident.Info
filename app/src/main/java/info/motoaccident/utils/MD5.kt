package info.motoaccident.utils

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

object MD5 {
    fun digest(text: String): String {
        try {
            val md = MessageDigest.getInstance("MD5")
            md.update(text.toByteArray())
            val digest = md.digest()
            val sb = StringBuilder()
            for (b in digest) {
                sb.append(String.format("%02x", b))
            }
            return sb.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            return ""
        }
    }
}