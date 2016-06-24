package info.motoaccident.controllers

import info.motoaccident.dictionaries.Role
import info.motoaccident.network.HttpRequestService
import info.motoaccident.network.modeles.auth.User
import rx.android.schedulers.AndroidSchedulers
import rx.subjects.PublishSubject

object UserController {
    val roleUpdated: PublishSubject<Boolean> = PublishSubject.create()

    private var user = User()

    var role: Role
        get() = user.role
        set(value) {
            user.role = value
            roleUpdated.onNext(true)
        }
    val id: Int
        get() = user.id

    //Flow control
    val userUpdated: PublishSubject<Boolean> = PublishSubject.create()

    init {
        if (PreferencesController.anonymous) role = Role.ANONYMOUS
    }

    fun logOff() {
        user = User()
    }

    fun auth(login: String = PreferencesController.login, password: String = PreferencesController.passHash) {
        when {
            role == Role.ANONYMOUS -> userUpdated.onNext(true)
            login.equals("") || password.equals("") -> userUpdated.onNext(false)
            else ->
                HttpRequestService
                        .api
                        .auth(login, password)
                        .retry(3)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                { requestResult ->
                                    //TODO error handling
                                    if (requestResult.error.equals("")) {
                                        user = requestResult.result
                                        userUpdated.onNext(role != Role.UNAUTHORIZED)
                                    } else {
                                        //TODO errors control
                                    }
                                },
                                //TODO errors control
                                { e -> e.printStackTrace() }
                                  )
        }
    }
}