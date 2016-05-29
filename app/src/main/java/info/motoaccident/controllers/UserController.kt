package info.motoaccident.controllers

import info.motoaccident.dictionaries.Role
import info.motoaccident.network.HttpRequestService
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

object UserController {
    var id = 0
        private set
    var role = Role.UNAUTHORIZED
        private set
    private var anonimous = PreferencesController.anonymous
        private set
    var needLogin = false;

    val isAuthorized: Observable<Boolean> = Observable.just(false)
            .repeatWhen { o -> o.delay(100, TimeUnit.MILLISECONDS) }
            .map { o -> role != Role.UNAUTHORIZED || needLogin }
            .filter { b -> b }
            .take(1)

    init {
        initialAuth();
    }

    fun logOff() {
        role = Role.UNAUTHORIZED
    }

    fun auth(login: String, password: String): Observable<Boolean> {

        return Observable.create { subscriber ->
            HttpRequestService.api
                    .auth(login, password)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { auth ->
                                //TODO user id
                                //TODO error handling
                                //TODO user User class instead of local properties
                                role = auth.result.role
                                subscriber.onNext(role != Role.UNAUTHORIZED)
                                subscriber.onCompleted()

                            })
        }
    }

    private fun initialAuth() {
        if (anonimous) {
            role = Role.ANONYMOUS
            return
        }
        if (PreferencesController.login.equals("")) {
            needLogin = true
            return
        }
        auth(PreferencesController.login, PreferencesController.passHash).subscribe { b -> needLogin = !b }
    }
}