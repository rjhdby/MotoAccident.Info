package info.motoaccident

import android.app.Activity
import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import android.util.Log
import info.motoaccident.controllers.LocationController
import rx.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class MyApplication : Application() {
    companion object {
        lateinit var context: Context
        var activity: Activity? = null
            private set
        val currentActivity: PublishSubject<Activity?> = PublishSubject.create()
        val onForeground: PublishSubject<Boolean> = PublishSubject.create()

        private val statusSubscription: PublishSubject<Boolean> = PublishSubject.create()
        private var currentStatus = false
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        LocationController.start()
        statusSubscription.debounce(2, TimeUnit.SECONDS).filter { b -> b != currentStatus }.subscribe { b -> currentStatus = b;onForeground.onNext(b);Log.d("STATUS",b.toString()) }
        currentActivity.subscribe { a -> activity = a;statusSubscription.onNext(a != null) }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}