package info.motoaccident.controllers

import info.motoaccident.dictionaries.*
import rx.subjects.PublishSubject

object PermissionController {
    var localPermissions = 0
    val permissionsUpdated: PublishSubject<Boolean> = PublishSubject.create()

    fun check(permission: Int): Boolean {
        buildLocalPermissions()
        return permission and localPermissions != 0
    }

    private fun buildLocalPermissions() {
        localPermissions = 0;
        when (UserController.role) {
            Role.READ_ONLY, Role.ANONYMOUS, Role.UNAUTHORIZED -> localPermissions = localPermissions or READONLY
            Role.STANDARD                                     -> localPermissions = localPermissions or STANDARD
            Role.MODERATOR                                    -> localPermissions = localPermissions or MODERATOR
            Role.DEVELOPER                                    -> localPermissions = localPermissions or DEVELOPER
        }
        if (phoneEnabled()) localPermissions = localPermissions or PHONE
        if (isLocationEnabled()) localPermissions = localPermissions or LOCATION
        if (isNotificationsEnabled()) localPermissions = localPermissions or NOTIFICATIONS
    }

    private fun phoneEnabled(): Boolean {
        //TODO implement
        return true
    }

    fun isLocationEnabled(): Boolean {
        return LocationController.isEnabled()
    }

    fun isNotificationsEnabled(): Boolean {
        //TODO implement
        return true
    }
}
