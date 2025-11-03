package app.olauncher.helper

import android.app.usage.UsageStatsManager
import android.content.Context
import java.util.*

class UsageTracker(private val context: Context) {

    fun getUsageTime(packageName: String): Long {
        val usm = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val calendar = Calendar.getInstance()
        val endTime = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        val startTime = calendar.timeInMillis

        val usageStatsList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime)
        for (usageStats in usageStatsList) {
            if (usageStats.packageName == packageName) {
                return usageStats.totalTimeInForeground
            }
        }
        return 0
    }
}
