package info.motoaccident.activity

import android.content.Context
import android.view.View
import rx.Observable
import rx.subjects.PublishSubject

interface ActivityInterface<T> {
    fun contentView(): T
    fun getPermittedResources(): Observable<Pair<View, Int>>
    fun getContext(): Context
    val readyForDecorate: PublishSubject<Boolean>
}