package info.motoaccident.activity

import android.content.Context
import android.view.View
import com.google.android.gms.maps.GoogleMap
import rx.Observable
import rx.subjects.PublishSubject

interface MapActivityInterface {
    fun contentView(): GoogleMap
    fun getPermittedResources(): Observable<Pair<View, Int>>
    fun getContext(): Context
    val mapReady: PublishSubject<Boolean>
}