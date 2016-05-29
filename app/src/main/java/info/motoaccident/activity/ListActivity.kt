package info.motoaccident.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import info.motoaccident.R
import info.motoaccident.network.HttpRequestService
import rx.android.schedulers.AndroidSchedulers

class ListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        HttpRequestService.api.list().observeOn(AndroidSchedulers.mainThread()).subscribe()
    }
}
