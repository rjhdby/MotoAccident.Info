package info.motoaccident.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import info.motoaccident.R
import info.motoaccident.controllers.ContentController
import info.motoaccident.controllers.UserController
import rx.Subscription

class StartActivity : AppCompatActivity() {
    lateinit private var userUpdateSubscription: Subscription

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        ContentController.reloadContent()
        userUpdateSubscription = UserController.userUpdated.subscribe { success ->
            if (success) startActivity(Intent(this, ListActivity::class.java))
            else startActivity(Intent(this, AuthActivity::class.java))
        }
        UserController.auth()
    }

    override fun onPause() {
        super.onPause()
        userUpdateSubscription.unsubscribe()
    }
}
