package info.motoaccident.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import info.motoaccident.MyApplication
import info.motoaccident.R
import info.motoaccident.controllers.ContentController
import info.motoaccident.controllers.UserController
import info.motoaccident.utils.runActivity
import rx.Subscription

class StartActivity : AppCompatActivity() {
    lateinit private var userUpdateSubscription: Subscription

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
    }

    override fun onResume() {
        super.onResume()
//        MyApplication.currentActivity.onNext(this)
        ContentController.reloadContent()
        userUpdateSubscription = UserController.userUpdated.subscribe { success ->
            if (success) runActivity(ListActivity::class.java)
            else runActivity(AuthActivity::class.java)
        }
        UserController.auth()
    }

    override fun onPause() {
        super.onPause()
        userUpdateSubscription.unsubscribe()
//        MyApplication.currentActivity.onNext(null)
    }
}
