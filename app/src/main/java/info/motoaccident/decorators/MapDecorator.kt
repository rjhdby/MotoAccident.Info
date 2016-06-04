package info.motoaccident.decorators

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import info.motoaccident.R
import info.motoaccident.activity.ActivityInterface
import info.motoaccident.controllers.ContentController
import info.motoaccident.controllers.PermissionController
import info.motoaccident.controllers.PreferencesController
import info.motoaccident.controllers.UserController
import info.motoaccident.network.modeles.list.Point
import info.motoaccident.utils.visible
import rx.Subscription

object MapDecorator : ViewDecorator<ActivityInterface<GoogleMap>> {
    lateinit private var target: ActivityInterface<GoogleMap>
    lateinit private var map: GoogleMap

    lateinit private var mapReadySubscription: Subscription
    lateinit private var contentUpdateSubscription: Subscription
    lateinit private var preferencesUpdateSubscription: Subscription
    lateinit private var roleUpdateSubscription: Subscription

    override fun start(target: ActivityInterface<GoogleMap>) {
        this.target = target
        updateInterface()
        mapReadySubscription = target.readyForDecorate.take(1).subscribe { p -> realStart() }
    }

    private fun realStart() {
        //TODO setup start position
        //TODO subscribe camera change
        map = target.contentView()
        updateDataSet()
        contentUpdateSubscription = ContentController.contentUpdated.subscribe { updateDataSet() }
        preferencesUpdateSubscription = PreferencesController.preferencesUpdated.subscribe { updateDataSet() }
        roleUpdateSubscription = UserController.userUpdated.subscribe { updateDataSet();updateInterface() }
    }

    override fun stop() {
        if (!mapReadySubscription.isUnsubscribed) mapReadySubscription.unsubscribe()
        else {
            contentUpdateSubscription.unsubscribe()
            preferencesUpdateSubscription.unsubscribe()
            roleUpdateSubscription.unsubscribe()
        }
    }

    private fun updateDataSet() {
        ContentController.observePoints().subscribe { list -> refreshMap(list) }
    }

    private fun refreshMap(list: List<Point>) {
        map.clear()
        for (point in list) {
            //TODO move to MarkerController
            val markerOptions = MarkerOptions()
                    .position(point.location)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.camera_small)) //TODO icons
                    .anchor(0.5f, 1f)
                    .alpha(1f)
                    .title(point.description)
            map.addMarker(markerOptions)
        }
        //TODO implementation
    }

    private fun updateInterface() {
        target.getPermittedResources()
                .subscribe { p -> p.first.visible(PermissionController.check(p.second)) }
    }
}