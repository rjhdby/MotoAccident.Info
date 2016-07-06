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
        val observable = when (source) {
            LOCATION     -> LocationController.locationUpdated
            ROLE         -> UserController.userUpdated
            PREFERENCES  -> PreferencesController.preferencesUpdated
            PERMISSIONS  -> PermissionController.permissionsUpdated
            CONTENT      -> ContentController.contentUpdated
            NOTIFICATION -> NotificationController.notificationReceived
        }

        subscribers.add(Pair(source, observable.subscribe({ l -> target(l!!) })))
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
