package info.motoaccident.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import com.jakewharton.rxbinding.view.RxView
import com.jakewharton.rxbinding.widget.RxTextView
import info.motoaccident.R
import info.motoaccident.controllers.PreferencesController
import info.motoaccident.controllers.UserController
import info.motoaccident.utils.MD5
import rx.Subscription

class AuthActivity : AppCompatActivity() {
    lateinit var loginField: EditText
    lateinit var passwordField: EditText
    lateinit var loginButton: Button
    lateinit var anonymousLoginButton: Button

    lateinit var loginFieldSubscription: Subscription
    lateinit var passwordFieldSubscription: Subscription
    lateinit var loginButtonSubscription: Subscription
    lateinit var anonymousLoginButtonSubscription: Subscription

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        loginField = this.findViewById(R.id.login_field) as EditText
        passwordField = this.findViewById(R.id.password_field) as EditText
        loginButton = this.findViewById(R.id.login_button) as Button
        anonymousLoginButton = this.findViewById(R.id.anonymous_login_button)as Button
    }

    override fun onResume() {
        super.onResume()
        loginFieldSubscription = RxTextView.textChanges(loginField).subscribe({ b -> loginButtonStateChange() })
        passwordFieldSubscription = RxTextView.textChanges(passwordField).subscribe({ b -> loginButtonStateChange() })
        loginButtonSubscription = RxView.clicks(loginButton).subscribe { b -> auth() }
        //TODO Anonymous
        anonymousLoginButtonSubscription = RxView.clicks(anonymousLoginButton).subscribe()
    }

    override fun onPause() {
        super.onPause()
        loginFieldSubscription.unsubscribe()
        passwordFieldSubscription.unsubscribe()
        loginButtonSubscription.unsubscribe()
        anonymousLoginButtonSubscription.unsubscribe()
    }

    private fun loginButtonStateChange() {
        loginButton.isEnabled = loginField.text.isNotBlank() && passwordField.text.isNotBlank()
    }

    private fun auth() {
        UserController.auth(loginField.text.toString(), MD5.digest(passwordField.text.toString()))
                .subscribe { success ->
                    if (success) {
                        PreferencesController.login = loginField.text.toString()
                        PreferencesController.passHash = MD5.digest(passwordField.text.toString())
                        startActivity(Intent(this, ListActivity::class.java))
                    }
                }
    }
}
