package app.olauncher.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AppRestriction(
    @PrimaryKey val packageName: String,
    val appName: String,
    val allowedDays: List<Int>, // 1=Monday, 7=Sunday
    val maxContinuousMinutes: Int,
    val cooldownMinutes: Int,
    val isActive: Boolean,
    val createdAt: Long,
    val overrideCount: Int,
    val lastOverrideAt: Long?
)
