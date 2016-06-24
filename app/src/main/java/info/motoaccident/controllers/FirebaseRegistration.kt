package info.motoaccident.controllers

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import info.motoaccident.controllers.Orchestrator.Source.ROLE
import info.motoaccident.network.HttpRequestService

class FirebaseRegistration : FirebaseInstanceIdService() {
    override fun onTokenRefresh() {
        super.onTokenRefresh()
        val refreshedToken = FirebaseInstanceId.getInstance().token ?: return;

        // TODO: Implement this method to send any registration to your app's servers.
//        sendRegistrationToServer(refreshedToken);
        val orchestrator = Orchestrator;
        orchestrator.subscribe(ROLE, {
            HttpRequestService.api.gcm(UserController.id, refreshedToken, "")
            orchestrator.unSubscribe()
            Log.d("TOKEN", refreshedToken);
            Log.d("TOKEN USER", UserController.id.toString())
        })
    }
}