package info.motoaccident.decorators

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import info.motoaccident.R
import info.motoaccident.activity.interfaces.DetailsActivityInterface
import info.motoaccident.controllers.ContentController
import info.motoaccident.controllers.LocationController
import info.motoaccident.controllers.Orchestrator
import info.motoaccident.controllers.Orchestrator.Source.*
import info.motoaccident.controllers.PermissionController
import info.motoaccident.decorators.ViewDecorator
import info.motoaccident.dictionaries.DEVELOPER
import info.motoaccident.dictionaries.MODERATOR
import info.motoaccident.dictionaries.STANDARD
import info.motoaccident.network.modeles.list.Message
import info.motoaccident.network.modeles.list.Point
import info.motoaccident.utils.asAge
import info.motoaccident.utils.asDistance
import info.motoaccident.utils.distance
import kotlinx.android.synthetic.main.message_row.view.*
import org.jetbrains.anko.enabled
import org.jetbrains.anko.onUiThread
//TODO create message
//TODO in place, on way list
//TODO history
object DetailsDecorator : ViewDecorator<DetailsActivityInterface> {
    lateinit private var target: DetailsActivityInterface

    private var orchestrator = Orchestrator;

    lateinit var point: Point
        private set

    override fun start(target: DetailsActivityInterface) {
        this.target = target
        point = ContentController.getPoint(target.id)
        if (point.id == 0) {
            abort()
            return
        }
        target.getContext().onUiThread {
            target.messagesView.layoutManager = LinearLayoutManager(target.getContext(), LinearLayoutManager.VERTICAL, false)
        }
        checkPermissionToView()
        uiDecorate()
        generalInformationDecorate(target)
        orchestrator.subscribe(ROLE, { uiDecorate(); checkPermissionToView() })
        orchestrator.subscribe(arrayOf(LOCATION, CONTENT, PREFERENCES), { generalInformationDecorate(target) })
    }

    override fun stop() {
        orchestrator.unSubscribe()
    }

    private fun uiDecorate() {
        target.messageInput.enabled = PermissionController.check(STANDARD or MODERATOR or DEVELOPER)
        target.sendMessage.isEnabled = PermissionController.check(STANDARD or MODERATOR or DEVELOPER)
    }

    private fun checkPermissionToView() {
        //TODO abort() if hidden and role not MODERATOR or DEVELOPER
    }

    private fun generalInformationDecorate(target: DetailsActivityInterface) {
        with(point) {
            target.type.text = type.toString()
            target.damage.text = damage.toString()
            target.address.text = address
            target.owner.text = owner
            target.description.text = description
            target.age.text = time.asAge()
            target.distance.text = location.distance(LocationController.lastLocation).asDistance()
        }
        target.getContext().onUiThread {
            target.messagesView.adapter = RecyclerViewAdapter(point.messages)
            target.messagesView.invalidate()
        }
    }

    private fun abort() {
        stop()
        //TODO alert, and return to ListActivity
    }

    private class RecyclerViewAdapter(var list: List<Message>) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.message_row, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount() = list.count()

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(list[position])
        }

        private class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            fun bind(message: Message) {
                with(message) {
                    itemView.messageOwner.text = owner
                    itemView.messageAge.text = timeStamp.asAge()
                    itemView.text.text = text
                }
            }
            //TODO moderator tools on long click
        }
    }
}