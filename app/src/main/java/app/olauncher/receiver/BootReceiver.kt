package app.olauncher.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import app.olauncher.service.AppRestrictionService

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val serviceIntent = Intent(context, AppRestrictionService::class.java)
            context.startForegroundService(serviceIntent)
        }
    }
}
