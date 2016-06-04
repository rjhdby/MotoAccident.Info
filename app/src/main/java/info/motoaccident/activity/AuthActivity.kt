package info.motoaccident.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import com.jakewharton.rxbinding.view.RxView
import com.jakewharton.rxbinding.widget.RxTextView
import info.motoaccident.MyApplication
import info.motoaccident.R
import info.motoaccident.controllers.PreferencesController
import info.motoaccident.controllers.UserController
import info.motoaccident.dictionaries.Role
import info.motoaccident.utils.*
import rx.Observable
import rx.Subscription

class AuthActivity : AppCompatActivity() {
    //TODO Registration
    private val loginField by bindView<EditText>(R.id.login_field)
    private val passwordField by bindView<EditText>(R.id.password_field)
    private val loginButton by bindView<Button>(R.id.login_button)
    private val anonymousLoginButton  by bindView<Button>(R.id.anonymous_login_button)

    //Flow control
    lateinit private var userUpdateSubscription: Subscription

    //Listeners
    lateinit private var textFieldsSubscription: Subscription
    lateinit private var loginButtonSubscription: Subscription
    lateinit private var anonymousLoginButtonSubscription: Subscription

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
    }

    override fun onResume() {
        super.onResume()
//        MyApplication.currentActivity.onNext(this)
        textFieldsSubscription = Observable.merge(RxTextView.textChanges(loginField), RxTextView.textChanges(passwordField))
                .subscribe({ b -> loginButton.isEnabled = loginField.isNotEmpty() && passwordField.isNotEmpty() })
        loginButtonSubscription = RxView.clicks(loginButton)
                .subscribe { b -> UserController.auth(loginField.value(), passwordField.md5()) }
        //TODO Anonymous
        anonymousLoginButtonSubscription = RxView.clicks(anonymousLoginButton).subscribe { anonymousPressed() }
        userUpdateSubscription = UserController.userUpdated.subscribe { success ->
            if (success) {
                PreferencesController.login = loginField.text.toString()
                PreferencesController.passHash = passwordField.md5()
                runActivity(ListActivity::class.java)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        textFieldsSubscription.unsubscribe()
        loginButtonSubscription.unsubscribe()
        anonymousLoginButtonSubscription.unsubscribe()
        userUpdateSubscription.unsubscribe()
//        MyApplication.currentActivity.onNext(null)
    }

    fun anonymousPressed() {
        AlertDialog.Builder(this)
                .setMessage("Функционал создания событий и сообщений будет недоступен")
                .setPositiveButton("ОК") { dialog, whichButton ->
                    UserController.role = Role.ANONYMOUS
                    startActivity(Intent(this, ListActivity::class.java))
                }
                .setNegativeButton("Войти и запомнить") { dialog, whichButton ->
                    PreferencesController.anonymous = true
                    UserController.role = Role.ANONYMOUS
                    startActivity(Intent(this, ListActivity::class.java))
                }.setNeutralButton("Отмена") { dialog, whichButton -> }
                .create().show()
    }
}
