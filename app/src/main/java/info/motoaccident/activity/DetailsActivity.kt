package info.motoaccident.activity

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import info.motoaccident.MyApplication
import info.motoaccident.R
import info.motoaccident.activity.interfaces.DetailsActivityInterface
import info.motoaccident.decorators.details.DetailsDecorator
import info.motoaccident.utils.bindView

class DetailsActivity : AppCompatActivity(), DetailsActivityInterface {
    override fun getContext(): Context {
        return this
    }

    override var id = 0
    override val type by bindView<TextView>(R.id.type)
    override val damage by bindView<TextView>(R.id.damage)
    override val address by bindView<TextView>(R.id.address)
    override val description by bindView<TextView>(R.id.description)
    override val owner by bindView<TextView>(R.id.owner)
    override val distance by bindView<TextView>(R.id.distance)
    override val age by bindView<TextView>(R.id.age)
    override val messageInput by bindView<EditText>(R.id.messageInputField)
    override val sendMessage by bindView<ImageButton>(R.id.sendMessageButton)
    override val messagesView by bindView<RecyclerView>(R.id.messages)

    private val toolbar by bindView<Toolbar>(R.id.toolbar)

    override fun onResume() {
        super.onResume()
        MyApplication.currentActivity.onNext(this)
        DetailsDecorator.start(this);
    }

    override fun onPause() {
        super.onPause()
        MyApplication.currentActivity.onNext(null)
        DetailsDecorator.stop()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id = intent.extras.getInt("id")
        setContentView(R.layout.activity_details)
        setSupportActionBar(toolbar);
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.details_activity_toolbar, menu);
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
        //TODO implementation
//            R.id.action_map -> runActivity(MapActivity::class.java)
//            R.id.action_settings -> runActivity(SettingsActivity::class.java)
        }
        return super.onOptionsItemSelected(item)
    }
}
