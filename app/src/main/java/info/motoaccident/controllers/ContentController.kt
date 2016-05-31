package info.motoaccident.controllers

import info.motoaccident.network.HttpRequestService
import info.motoaccident.network.modeles.list.Point
import rx.Observable
import rx.subjects.PublishSubject
import java.util.*

object ContentController {
    var points: List<Point> = ArrayList()
    var contentUpdated: PublishSubject<Boolean> = PublishSubject.create()
    //TODO subscribe notifications
    //TODO subscribe accident created
    //TODO subscribe message created
    fun reloadContent() {
        HttpRequestService.api
                .list()
                .retry(3)
                .subscribe({
                               requestResult ->
                               if (requestResult.error.equals("")) {
                                   points = requestResult.result.sortedByDescending { p -> p.time }
                                   contentUpdated.onNext(true)
                               } else {
                                   //TODO errors control
                               }
                           },
                        //TODO errors control
                           { e -> e.printStackTrace() })
    }

    fun observePoints(): Observable<Point> {
        return Observable.from(points)
    }
}