package info.motoaccident.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import info.motoaccident.R
import info.motoaccident.controllers.ContentController
import info.motoaccident.controllers.UserController

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        ContentController.firstFetch()
        UserController.isAuthorized.subscribe { b ->
            if (UserController.needLogin) startActivity(Intent(this, AuthActivity::class.java))
            else startActivity(Intent(this, ListActivity::class.java))
        }
    }
}
