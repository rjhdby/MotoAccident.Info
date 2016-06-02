package info.motoaccident.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import com.google.android.gms.maps.GoogleMap
import rx.Observable
import rx.subjects.PublishSubject

interface MapActivityInterface {
    fun contentView(): GoogleMap
    fun getContext(): Context
    fun getPermittedResources(): Observable<Pair<View, Int>>
    fun runActivity(activity: Class<*>, bundle: Bundle = Bundle.EMPTY)

    val mapReady: PublishSubject<Boolean>
}