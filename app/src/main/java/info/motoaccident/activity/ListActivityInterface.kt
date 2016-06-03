package info.motoaccident.activity

import android.content.Context
import android.view.View
import rx.Observable

interface ListActivityInterface {
    fun contentView(): View
    fun getPermittedResources(): Observable<Pair<View, Int>>
    fun getContext(): Context
}