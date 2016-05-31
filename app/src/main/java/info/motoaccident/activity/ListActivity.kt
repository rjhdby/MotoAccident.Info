package info.motoaccident.activity

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import info.motoaccident.R
import info.motoaccident.decorators.ListDecorator
import info.motoaccident.utils.bindView

class ListActivity : AppCompatActivity(), ListActivityInterface {
    val listView by bindView<RecyclerView>(R.id.list_view)

    override fun getContext(): Context = this

    override fun listView(): RecyclerView = listView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        val myToolbar = findViewById(R.id.toolbar) as Toolbar;
        setSupportActionBar(myToolbar);
    }

    override fun onResume() {
        super.onResume()
        ListDecorator.start(this)
    }

    override fun onPause() {
        super.onPause()
        ListDecorator.stop()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.list_activity_toolbar, menu);
        return true;
    }
}
