package app.olauncher.service

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import app.olauncher.data.AppRestrictionDatabase
import app.olauncher.helper.UsageTracker
import app.olauncher.ui.BlockOverlayActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit

class AppRestrictionService : AccessibilityService() {

    private lateinit var usageTracker: UsageTracker

    override fun onCreate() {
        super.onCreate()
        usageTracker = UsageTracker(this)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val packageName = event.packageName?.toString() ?: return

            CoroutineScope(Dispatchers.IO).launch {
                val restriction = AppRestrictionDatabase.getDatabase(applicationContext).appRestrictionDao().getRestriction(packageName)
                if (restriction != null && restriction.isActive) {
                    val calendar = Calendar.getInstance()
                    val today = calendar.get(Calendar.DAY_OF_WEEK)
                    val currentTime = System.currentTimeMillis()

                    // Cooldown check
                    restriction.lastOverrideAt?.let { lastOverrideTime ->
                        val cooldownMillis = TimeUnit.MINUTES.toMillis(restriction.cooldownMinutes.toLong())
                        if (currentTime - lastOverrideTime < cooldownMillis) {
                            val remainingTime = cooldownMillis - (currentTime - lastOverrideTime)
                            launchBlocker(
                                packageName,
                                restriction.appName,
                                "Cooldown period active. Time remaining: ${TimeUnit.MILLISECONDS.toMinutes(remainingTime)} minutes"
                            )
                            return@launch
                        }
                    }

                    if (restriction.allowedDays.contains(today)) {
                        val usageTime = usageTracker.getUsageTime(packageName)
                        if (usageTime > TimeUnit.MINUTES.toMillis(restriction.maxContinuousMinutes.toLong())) {
                            launchBlocker(
                                packageName,
                                restriction.appName,
                                "You have exceeded the maximum usage time."
                            )
                        }
                    } else {
                        launchBlocker(
                            packageName,
                            restriction.appName,
                            "This app is not allowed to be used today."
                        )
                    }
                }
            }
        }
    }

    private fun launchBlocker(packageName: String, appName: String, reason: String) {
        val intent = Intent(this, BlockOverlayActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            putExtra("packageName", packageName)
            putExtra("appName", appName)
            putExtra("reason", reason)
        }
        startActivity(intent)
    }

    override fun onInterrupt() {
        // Not implemented
    }
}
