package info.motoaccident.controllers

import info.motoaccident.network.HttpRequestService
import info.motoaccident.network.modeles.list.Point
import rx.Observable
import java.util.*

object ContentController {
    var points: List<Point> = ArrayList()

    fun fetch(): Observable<Boolean> {
        return Observable.create { subscriber ->
            HttpRequestService.api
                    .list()
                    .subscribe({
                                   requestResult ->
                                   //TODO errors control
                                   points = requestResult.result
                                   subscriber.onNext(true)
                                   subscriber.onCompleted()
                               })
        }
    }

    fun firstFetch() {
        HttpRequestService.api
                .list()
                .subscribe({ requestResult -> points = requestResult.result.sortedByDescending { p -> p.time } })
    }

    fun getPoints(): Observable<Point> {
        return Observable.from(points)
    }
}
