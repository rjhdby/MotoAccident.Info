package info.motoaccident.decorators

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import info.motoaccident.R
import info.motoaccident.activity.ListActivityInterface
import info.motoaccident.controllers.ContentController
import info.motoaccident.controllers.PreferencesController
import info.motoaccident.controllers.UserController
import info.motoaccident.dictionaries.AccidentDamage
import info.motoaccident.dictionaries.AccidentStatus
import info.motoaccident.dictionaries.AccidentType
import info.motoaccident.dictionaries.Role
import info.motoaccident.network.modeles.list.Point
import info.motoaccident.utils.asAge
import info.motoaccident.utils.asTimeIntervalFromCurrent
import info.motoaccident.utils.distance
import kotlinx.android.synthetic.main.list_view_row.view.*
import org.jetbrains.anko.onUiThread
import rx.Observable
import rx.Subscription
import java.util.*

object ListDecorator : ViewDecorator<ListActivityInterface> {
    lateinit private var target: ListActivityInterface
    lateinit private var contentUpdateSubscription: Subscription
    lateinit private var preferencesUpdateSubscription: Subscription
    lateinit private var roleUpdateSubscription: Subscription

    override fun start(target: ListActivityInterface) {
        this.target = target
        //TODO subscribe location update
        //TODO subscribe permission update
        //TODO interface decorate
        target.getContext().onUiThread {
            target.listView().layoutManager = LinearLayoutManager(target.getContext(), LinearLayoutManager.VERTICAL, false)
        }
        updateInterface()
        updateDataSet(ContentController.observePoints())
        preferencesUpdateSubscription = PreferencesController.preferencesUpdated.subscribe { updateDataSet(ContentController.observePoints()) }
        contentUpdateSubscription = ContentController.contentUpdated.subscribe { updateDataSet(ContentController.observePoints()) }
        roleUpdateSubscription = UserController.userUpdated.subscribe { updateInterface(); updateDataSet(ContentController.observePoints()) }
        ContentController.reloadContent()
    }

    private fun updateDataSet(observer: Observable<Point>) {
        val list = ArrayList<Point>()
        observer.filter { p -> p.status != AccidentStatus.HIDDEN || (UserController.role == Role.MODERATOR || UserController.role == Role.DEVELOPER) }
                .filter { p -> p.status != AccidentStatus.ENDED || PreferencesController.ended }
                .filter { p -> p.type != AccidentType.OTHER || PreferencesController.other }
                .filter { p -> p.type != AccidentType.BREAK || PreferencesController.breaks }
                .filter { p -> p.type != AccidentType.STEAL || PreferencesController.steals }
                .filter { p -> p.location().distance(PreferencesController.lastLocation) < PreferencesController.showRadius * 1000 }
                .filter { p -> !p.isAccident() || PreferencesController.accidents }
                .filter { p -> (p.med != AccidentDamage.HEAVY && p.med != AccidentDamage.LETHAL) || PreferencesController.heavy }
                .filter { p -> p.time.asTimeIntervalFromCurrent() / 3600 < PreferencesController.maxAge }
                .subscribe ({ p -> list.add(p) }, { e -> e.printStackTrace() }, { refreshRecyclerView(list) })
    }

    private fun updateInterface() {

    }

    private fun refreshRecyclerView(list: ArrayList<Point>) {
        target.getContext().onUiThread {
            target.listView().adapter = RecyclerViewAdapter(list)
            target.listView().invalidate()
        }
    }

    override fun stop() {
        contentUpdateSubscription.unsubscribe()
        preferencesUpdateSubscription.unsubscribe()
        roleUpdateSubscription.unsubscribe()
        //TODO unSubscribe location update
        //TODO unSubscribe permission update
    }

    private class RecyclerViewAdapter(var list: ArrayList<Point>) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

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
                    itemView.address.text = address
                    itemView.owner.text = owner
                    itemView.description.text = description
                    itemView.age.text = time.asAge()
                    itemView.distance.text = location().distance(PreferencesController.lastLocation).toString()
                }
            }
        }
    }
}