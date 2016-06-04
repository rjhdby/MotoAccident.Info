package info.motoaccident.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import com.jakewharton.rxbinding.view.RxView
import info.motoaccident.MyApplication
import info.motoaccident.R
import info.motoaccident.decorators.ListDecorator
import info.motoaccident.dictionaries.DEVELOPER
import info.motoaccident.dictionaries.MODERATOR
import info.motoaccident.dictionaries.PHONE
import info.motoaccident.dictionaries.STANDARD
import info.motoaccident.utils.bindView
import info.motoaccident.utils.runActivity
import rx.Observable
import rx.Subscription
import rx.subjects.PublishSubject

class ListActivity : AppCompatActivity(), ActivityInterface<RecyclerView> {
    private val createAccidentButton by bindView<ImageButton>(R.id.create_acc)
    private val callButton by bindView<ImageButton>(R.id.call)
    private val toolbar by bindView<Toolbar>(R.id.toolbar)

    lateinit private var callButtonSubscription: Subscription
    lateinit private var createAccidentButtonSubscription: Subscription

    override val readyForDecorate: PublishSubject<Boolean> = PublishSubject.create()

    override fun getPermittedResources(): Observable<Pair<View, Int>> = Observable.just(
            Pair(createAccidentButton, STANDARD or MODERATOR or DEVELOPER),
            Pair(callButton, PHONE))

    val listView by bindView<RecyclerView>(R.id.list_view)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        setSupportActionBar(toolbar);
    }

    override fun onResume() {
        super.onResume()
        MyApplication.currentActivity.onNext(this)
        ListDecorator.start(this)
        callButtonSubscription = RxView.clicks(callButton).subscribe { callPressed() }
        createAccidentButtonSubscription = RxView.clicks(callButton).subscribe { createPressed() }
    }

    override fun onPause() {
        super.onPause()
        ListDecorator.stop()
        callButtonSubscription.unsubscribe()
        createAccidentButtonSubscription.unsubscribe()
        MyApplication.currentActivity.onNext(null)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_map -> runActivity(MapActivity::class.java)
            R.id.action_settings -> runActivity(SettingsActivity::class.java)
        }
        return super.onOptionsItemSelected(item)
    }

    fun callPressed() {
        //TODO implementation
    }

    fun createPressed() {
        //TODO implementation
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.list_activity_toolbar, menu);
        return true;
    }

    override fun contentView(): RecyclerView = listView

    override fun getContext(): Context = this

    override fun onBackPressed() {
        val backToHome: Intent = Intent(Intent.ACTION_MAIN);
        backToHome.addCategory(Intent.CATEGORY_HOME);
        backToHome.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
        startActivity(backToHome);
    }
}
