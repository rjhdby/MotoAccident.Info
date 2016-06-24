package info.motoaccident.controllers

import info.motoaccident.dictionaries.AccidentDamage
import info.motoaccident.dictionaries.AccidentStatus
import info.motoaccident.dictionaries.AccidentType
import info.motoaccident.dictionaries.Role
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
        return Observable.from(points).filter { p -> p.status != AccidentStatus.HIDDEN || (UserController.role == Role.MODERATOR || UserController.role == Role.DEVELOPER) }
                .filter { p -> p.status != AccidentStatus.ENDED || PreferencesController.ended }
                .filter { p -> p.type != AccidentType.OTHER || PreferencesController.other }
                .filter { p -> p.type != AccidentType.BREAK || PreferencesController.breaks }
                .filter { p -> p.type != AccidentType.STEAL || PreferencesController.steals }
                .filter { p -> p.location.distance(LocationController.lastLocation) < PreferencesController.showRadius * 1000 }
                .filter { p -> !p.isAccident() || PreferencesController.accidents }
                .filter { p -> (p.damage != AccidentDamage.HEAVY && p.damage != AccidentDamage.LETHAL) || PreferencesController.heavy }
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