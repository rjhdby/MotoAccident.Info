package info.motoaccident.activity

import android.content.Context
import android.support.v7.widget.RecyclerView

interface ListActivityInterface {
    fun listView(): RecyclerView
    fun getContext(): Context
}