package app.olauncher.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [AppRestriction::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppRestrictionDatabase : RoomDatabase() {

    abstract fun appRestrictionDao(): AppRestrictionDao

    companion object {
        @Volatile
        private var INSTANCE: AppRestrictionDatabase? = null

        fun getDatabase(context: Context): AppRestrictionDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppRestrictionDatabase::class.java,
                    "app_restriction_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
