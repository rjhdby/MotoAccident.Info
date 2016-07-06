package info.motoaccident.controllers

import info.motoaccident.dictionaries.AccidentDamage.HEAVY
import info.motoaccident.dictionaries.AccidentDamage.LETHAL
import info.motoaccident.dictionaries.AccidentStatus.ENDED
import info.motoaccident.dictionaries.AccidentStatus.HIDDEN
import info.motoaccident.dictionaries.AccidentType.*
import info.motoaccident.dictionaries.Role.DEVELOPER
import info.motoaccident.dictionaries.Role.MODERATOR
import info.motoaccident.network.HttpRequestService
import info.motoaccident.network.modeles.list.Point
import info.motoaccident.utils.asTimeIntervalFromCurrent
import info.motoaccident.utils.distance
import rx.Observable
import rx.subjects.PublishSubject
import java.util.*

object ContentController {
    private var points: List<Point> = ArrayList()
    val contentUpdated: PublishSubject<Boolean> = PublishSubject.create()
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

    fun observePoints(): Observable<List<Point>> {
        return Observable.from(points)
                .filter { p -> p.status != HIDDEN || (UserController.role == MODERATOR || UserController.role == DEVELOPER) }
                .filter { p -> p.status != ENDED || PreferencesController.ended }
                .filter { p -> p.type != OTHER || PreferencesController.other }
                .filter { p -> p.type != BREAK || PreferencesController.breaks }
                .filter { p -> p.type != STEAL || PreferencesController.steals }
                .filter { p -> p.location.distance(LocationController.lastLocation) < PreferencesController.showRadius * 1000 }
                .filter { p -> !p.isAccident() || PreferencesController.accidents }
                .filter { p -> (p.damage != HEAVY && p.damage != LETHAL) || PreferencesController.heavy }
                .filter { p -> p.time.asTimeIntervalFromCurrent() / 3600 < PreferencesController.maxAge }
                .toList()
    }

    fun getPoint(id: Int): Point {
        for (point in points) {
            if (point.id == id) return point
        }
        return Point()
    }
}