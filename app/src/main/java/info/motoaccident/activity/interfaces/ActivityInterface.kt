package info.motoaccident.activity.interfaces

import android.content.Context
import android.view.View
import rx.Observable

interface ActivityInterface<T> {
    fun contentView(): T
    fun getPermittedResources(): Observable<Pair<View, Int>>
    fun getContext(): Context
}