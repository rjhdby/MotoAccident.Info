package info.motoaccident.controllers

import info.motoaccident.controllers.Orchestrator.Source.*
import rx.Subscription


object Orchestrator {

    private var subscribers = mutableListOf<Pair<Source, Subscription>>();

    enum class Source {
        LOCATION,
        PERMISSIONS,
        CONTENT,
        PREFERENCES,
        ROLE,
        NOTIFICATION
    }

    fun subscribe(source: Source, target: (item: Any) -> Unit) {

        subscribers.add(Pair(source,
                             when (source) {
                                 LOCATION     -> LocationController.locationUpdated.subscribe({ l -> target(l) })
                                 ROLE         -> UserController.userUpdated.subscribe({ l -> target(l) })
                                 PREFERENCES  -> PreferencesController.preferencesUpdated.subscribe({ l -> target(l) })
                                 PERMISSIONS  -> PermissionController.permissionsUpdated.subscribe({ l -> target(l) })
                                 CONTENT      -> ContentController.contentUpdated.subscribe({ l -> target(l) })
                                 NOTIFICATION -> NotificationController.notificationReceived.subscribe({ l -> target(l) })
                             }
                            ))
    }

    fun subscribe(sources: Array<Source>, target: (item: Any) -> Unit) {
        sources.forEach { subscribe(it, target) }
    }

    fun unSubscribe(source: Source) {
        subscribers.forEach {
            if (it.first == source) {
                it.second.unsubscribe()
                subscribers.remove(it)
            }
        }
    }

    fun unSubscribe(sources: Array<Source>) {
        sources.forEach { unSubscribe(it) }
    }

    fun unSubscribe() {
        subscribers.forEach { it.second.unsubscribe() }
        subscribers.clear()
    }
}
