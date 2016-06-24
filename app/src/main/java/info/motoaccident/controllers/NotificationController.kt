package info.motoaccident.controllers

import com.google.firebase.iid.FirebaseInstanceIdService
import com.google.firebase.messaging.FirebaseMessagingService
import rx.subjects.PublishSubject


class NotificationController: FirebaseMessagingService() {
    companion object {
        val notificationReceived: PublishSubject<Boolean> = PublishSubject.create()
    }


}