package info.motoaccident.decorators

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import info.motoaccident.R
import info.motoaccident.activity.ActivityInterface
import info.motoaccident.controllers.*
import info.motoaccident.network.modeles.list.Point
import info.motoaccident.utils.asAge
import info.motoaccident.utils.asDistance
import info.motoaccident.utils.distance
import info.motoaccident.utils.visible
import kotlinx.android.synthetic.main.list_view_row.view.*
import org.jetbrains.anko.onUiThread
import rx.Subscription

object ListDecorator : ViewDecorator<ActivityInterface<RecyclerView>> {
    lateinit private var target: ActivityInterface<RecyclerView>
    lateinit private var contentUpdateSubscription: Subscription
    lateinit private var preferencesUpdateSubscription: Subscription
    lateinit private var roleUpdateSubscription: Subscription
    lateinit private var locationUpdateSubscription: Subscription

    lateinit private var listView: RecyclerView

    override fun start(target: ActivityInterface<RecyclerView>) {
        this.target = target
        locationUpdateSubscription = LocationController.locationUpdated.subscribe { updateDataSet() }
        //TODO subscribe permission update
        listView = target.contentView()
        target.getContext().onUiThread {
            listView.layoutManager = LinearLayoutManager(target.getContext(), LinearLayoutManager.VERTICAL, false)
        }
        updateInterface()
        updateDataSet()
        preferencesUpdateSubscription = PreferencesController.preferencesUpdated.subscribe { updateDataSet() }
        contentUpdateSubscription = ContentController.contentUpdated.subscribe { updateDataSet() }
        roleUpdateSubscription = UserController.userUpdated.subscribe { updateInterface(); updateDataSet() }
        ContentController.reloadContent()
    }

    private fun updateDataSet() = ContentController.observePoints().subscribe { list -> refreshRecyclerView(list) }

    private fun updateInterface() {
        target.getPermittedResources()
                .subscribe { p -> p.first.visible(PermissionController.check(p.second)) }
    }

    private fun refreshRecyclerView(list: List<Point>) {
        target.getContext().onUiThread {
            listView.adapter = RecyclerViewAdapter(list)
            listView.invalidate()
        }
    }

    override fun stop() {
        contentUpdateSubscription.unsubscribe()
        preferencesUpdateSubscription.unsubscribe()
        roleUpdateSubscription.unsubscribe()
        locationUpdateSubscription.unsubscribe()
        //TODO unSubscribe permission update
    }

    private class RecyclerViewAdapter(var list: List<Point>) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_view_row, parent, false)
            //TODO OnClickListener
            //TODO list_view_row, resolve 'age' problem
            return ViewHolder(view)
        }

        override fun getItemCount() = list.count()

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(list[position])
        }

        private class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            fun bind(point: Point) {
                with(point) {
                    //TODO type, medicine, owner
                    itemView.address.text = address
                    itemView.owner.text = owner
                    itemView.description.text = description
                    itemView.age.text = time.asAge()
                    itemView.distance.text = location.distance(LocationController.lastLocation).asDistance()
                }
            }
        }
    }
}