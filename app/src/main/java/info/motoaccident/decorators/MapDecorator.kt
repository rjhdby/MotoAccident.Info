package info.motoaccident.decorators

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import info.motoaccident.R
import info.motoaccident.activity.interfaces.ActivityInterface
import info.motoaccident.controllers.ContentController
import info.motoaccident.controllers.LocationController
import info.motoaccident.controllers.Orchestrator
import info.motoaccident.controllers.Orchestrator.Source.*
import info.motoaccident.controllers.PermissionController
import info.motoaccident.network.modeles.list.Point
import info.motoaccident.utils.visible
import rx.Observable

object MapDecorator : ViewDecorator<ActivityInterface<SupportMapFragment>>, OnMapReadyCallback {
    lateinit private var target: ActivityInterface<SupportMapFragment>
    private var map: GoogleMap? = null

    private var orchestrator = Orchestrator;

    override fun start(target: ActivityInterface<SupportMapFragment>) {
        this.target = target
        updateInterface()
        if (map == null) target.contentView().getMapAsync(this)
        else realStart()
    }

    private fun realStart() {
        //TODO setup start position
        //TODO subscribe camera change
        updateDataSet()
        orchestrator.subscribe(arrayOf(ROLE, LOCATION, CONTENT, PREFERENCES), { updateDataSet() })
        orchestrator.subscribe(ROLE, { updateInterface() })
    }

    override fun stop() {
        orchestrator.unSubscribe()
    }

    private fun updateDataSet() {
        ContentController.observePoints().subscribe { list -> refreshMap(list) }
    }

    private fun refreshMap(list: List<Point>) {
        map!!.clear()
        Observable.from(list).subscribe { point -> map!!.addMarker(marker(point)) }
    }

    private fun updateInterface() {
        target.getPermittedResources()
                .subscribe { p -> p.first.visible(PermissionController.check(p.second)) }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        LocationController.locationEnabled.take(1).subscribe { b -> if (b) map!!.isMyLocationEnabled = true }
        LocationController.requestPermission(target.getContext())
        realStart()
    }

    private fun marker(point: Point): MarkerOptions {
        return MarkerOptions()
                .position(point.location)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.camera_small)) //TODO icons
                .anchor(0.5f, 1f)
                .alpha(1f)
                .title(point.description)
    }
}