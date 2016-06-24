package info.motoaccident.activity.interfaces

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView

interface DetailsActivityInterface {
    val id: Int

    val type: TextView
    val damage: TextView
    val address: TextView
    val description: TextView
    val owner: TextView
    val distance: TextView
    val age: TextView

    val messagesView: RecyclerView

    val messageInput: EditText
    val sendMessage: ImageButton

    fun getContext(): Context
}