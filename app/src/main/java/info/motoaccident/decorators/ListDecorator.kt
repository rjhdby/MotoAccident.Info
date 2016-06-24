package info.motoaccident.decorators

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding.view.RxView
import info.motoaccident.R
import info.motoaccident.activity.DetailsActivity
import info.motoaccident.activity.interfaces.ActivityInterface
import info.motoaccident.controllers.ContentController
import info.motoaccident.controllers.LocationController
import info.motoaccident.controllers.Orchestrator
import info.motoaccident.controllers.Orchestrator.Source.*
import info.motoaccident.controllers.PermissionController
import info.motoaccident.network.modeles.list.Point
import info.motoaccident.utils.*
import kotlinx.android.synthetic.main.list_row.view.*
import org.jetbrains.anko.onUiThread

object ListDecorator : ViewDecorator<ActivityInterface<RecyclerView>> {
    lateinit private var target: ActivityInterface<RecyclerView>

    private var orchestrator = Orchestrator;

    lateinit private var listView: RecyclerView

    override fun start(target: ActivityInterface<RecyclerView>) {
        this.target = target
        //TODO subscribe permission update
        listView = target.contentView()
        target.getContext().onUiThread {
            listView.layoutManager = LinearLayoutManager(target.getContext(), LinearLayoutManager.VERTICAL, false)
        }
        updateInterface()
        updateDataSet()
        ContentController.reloadContent()
        orchestrator.subscribe(arrayOf(ROLE, LOCATION, CONTENT, PREFERENCES), { updateDataSet() })
        orchestrator.subscribe(ROLE, { updateInterface() })
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
        orchestrator.unSubscribe()
        //TODO unSubscribe permission update
    }

    private class RecyclerViewAdapter(var list: List<Point>) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_row, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount() = list.count()

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(list[position])
        }

        private class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            fun bind(point: Point) {
                with(point) {
                    itemView.type.text = type.toString()
                    itemView.damage.text = damage.toString()
                    itemView.address.text = address
                    itemView.owner.text = owner
                    itemView.description.text = description
                    itemView.age.text = time.asAge()
                    itemView.distance.text = location.distance(LocationController.lastLocation).asDistance()
                    itemView.messages.text = messages.count().toString()
                }
                RxView.clicks(itemView).subscribe {
                    val bundle = Bundle()
                    bundle.putInt("id", point.id)
                    (target.getContext() as AppCompatActivity).runActivity(DetailsActivity::class.java, bundle)
                }
                //TODO moderator tools on long click
            }
        }
    }
}