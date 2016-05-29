package info.motoaccident.decorators

import android.content.Context
import info.motoaccident.controllers.ContentController
import info.motoaccident.controllers.PreferencesController
import info.motoaccident.dictionaries.AccidentType
import info.motoaccident.network.modeles.list.Point
import info.motoaccident.utils.Distance

object ListDecorator {
    private var context: Context? = null
    fun start(context: Context) {
        this.context = context
        //TODO subscribe location update
        //TODO subscribe content update
        //TODO subscribe preferences update
        //TODO subscribe permission update
        //TODO interface decorate
        ContentController.getPoints()
                .filter { p -> p.type == AccidentType.MOTO_AUTO && PreferencesController.accidents }
                .filter { p -> p.type == AccidentType.MOTO_MAN && PreferencesController.accidents }
                .filter { p -> p.type == AccidentType.MOTO_MOTO && PreferencesController.accidents }
                .filter { p -> p.type == AccidentType.SOLO && PreferencesController.accidents }
                .filter { p -> p.type == AccidentType.BREAK && PreferencesController.breaks }
                .filter { p -> p.type == AccidentType.STEAL && PreferencesController.steals }
                .filter { p -> p.type == AccidentType.OTHER && PreferencesController.other }
                .filter { p -> Distance.beetwen(p.location(), PreferencesController.lastLocation) < PreferencesController.showRadius * 1000 }
                //TODO time filter
                //TODO medicine filter
                //TODO ended filter
                .subscribe { p -> addToList(p) }
    }

    fun stop() {
        if (context == null) return
        //TODO unSubscribe location update
        //TODO unSubscribe content update
        //TODO unSubscribe preferences update
        //TODO unSubscribe permission update
    }

    private fun addToList(point: Point) {
//TODO implementation
    }
}