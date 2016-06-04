package info.motoaccident.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.jakewharton.rxbinding.view.RxView
import info.motoaccident.MyApplication
import info.motoaccident.R
import info.motoaccident.controllers.LocationController
import info.motoaccident.decorators.MapDecorator
import info.motoaccident.dictionaries.DEVELOPER
import info.motoaccident.dictionaries.MODERATOR
import info.motoaccident.dictionaries.PHONE
import info.motoaccident.dictionaries.STANDARD
import info.motoaccident.utils.bindView
import info.motoaccident.utils.runActivity
import rx.Observable
import rx.Subscription
import rx.subjects.PublishSubject

class MapActivity : AppCompatActivity(), OnMapReadyCallback, ActivityInterface<GoogleMap> {
    private val createAccidentButton by bindView<ImageButton>(R.id.create_acc)
    private val callButton by bindView<ImageButton>(R.id.call)
    private val toolbar by bindView<Toolbar>(R.id.toolbar)

    lateinit private var mapFragment: SupportMapFragment

    lateinit private var callButtonSubscription: Subscription
    lateinit private var createAccidentButtonSubscription: Subscription

    override val readyForDecorate: PublishSubject<Boolean> = PublishSubject.create()

    private var map: GoogleMap? = null //TODO move to MapDecorator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.retainInstance = true
        setSupportActionBar(toolbar);
    }

    override fun onResume() {
        super.onResume()
        MyApplication.currentActivity.onNext(this)
        MapDecorator.start(this)
        if (map == null) mapFragment.getMapAsync(this)
        else readyForDecorate.onNext(true)
        callButtonSubscription = RxView.clicks(callButton).subscribe { callPressed() }
        createAccidentButtonSubscription = RxView.clicks(callButton).subscribe { createPressed() }
    }

    override fun onPause() {
        super.onPause()
        MapDecorator.stop()
        callButtonSubscription.unsubscribe()
        createAccidentButtonSubscription.unsubscribe()
        MyApplication.currentActivity.onNext(null)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        LocationController.locationEnabled.take(1).subscribe { b -> if (b) map!!.isMyLocationEnabled = true }
        LocationController.requestPermission(this)
        readyForDecorate.onNext(true)
    }

    override fun contentView(): GoogleMap {
        return map!!
    }

    override fun getPermittedResources(): Observable<Pair<View, Int>> = Observable.just(
            Pair(createAccidentButton, STANDARD or MODERATOR or DEVELOPER),
            Pair(callButton, PHONE))

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_list -> runActivity(ListActivity::class.java)
            R.id.action_settings -> runActivity(SettingsActivity::class.java)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.map_activity_toolbar, menu);
        return true;
    }

    fun callPressed() {
        //TODO implementation
    }

    fun createPressed() {
        //TODO implementation
    }

    override fun getContext(): Context = this

    override fun onBackPressed() {
        val backToHome: Intent = Intent(Intent.ACTION_MAIN);
        backToHome.addCategory(Intent.CATEGORY_HOME);
        backToHome.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
        startActivity(backToHome);
    }
}
