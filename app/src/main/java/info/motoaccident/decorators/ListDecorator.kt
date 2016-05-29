package info.motoaccident.decorators

import android.content.Context
import info.motoaccident.controllers.ContentController
import info.motoaccident.controllers.PreferencesController
import info.motoaccident.controllers.UserController
import info.motoaccident.dictionaries.AccidentDamage
import info.motoaccident.dictionaries.AccidentStatus
import info.motoaccident.dictionaries.AccidentType
import info.motoaccident.dictionaries.Role
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
                .filter { p -> p.status == AccidentStatus.HIDDEN && (UserController.role != Role.MODERATOR || UserController.role != Role.DEVELOPER) }
                .filter { p -> p.status == AccidentStatus.ENDED && PreferencesController.ended }
                .filter { p -> p.type == AccidentType.OTHER && PreferencesController.other }
                .filter { p -> p.type == AccidentType.BREAK && PreferencesController.breaks }
                .filter { p -> p.type == AccidentType.STEAL && PreferencesController.steals }
                .filter { p -> Distance.beetwen(p.location(), PreferencesController.lastLocation) < PreferencesController.showRadius * 1000 }
                .filter { p -> p.type == AccidentType.MOTO_AUTO && PreferencesController.accidents }
                .filter { p -> p.type == AccidentType.MOTO_MAN && PreferencesController.accidents }
                .filter { p -> p.type == AccidentType.MOTO_MOTO && PreferencesController.accidents }
                .filter { p -> p.type == AccidentType.SOLO && PreferencesController.accidents }
                .filter { p -> (p.med == AccidentDamage.HEAVY || p.med == AccidentDamage.LETHAL) && PreferencesController.heavy }
                //TODO time filter
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