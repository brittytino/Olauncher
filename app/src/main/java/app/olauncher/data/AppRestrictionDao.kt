package app.olauncher.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AppRestrictionDao {
    @Query("SELECT * FROM apprestriction")
    fun getAll(): Flow<List<AppRestriction>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(appRestriction: AppRestriction)

    @Query("DELETE FROM apprestriction WHERE packageName = :packageName")
    suspend fun delete(packageName: String)
}
