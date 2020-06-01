package sga111.seng440.crapchat.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PreferenceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(preference: Preference)

    @Query("SELECT * from preferences_table WHERE key = :key")
    fun retrieve(key: String): Preference

}