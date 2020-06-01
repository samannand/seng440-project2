package sga111.seng440.crapchat.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = arrayOf(Preference::class), version = 1, exportSchema = false )
public abstract class CrapchatRoomDatabase : RoomDatabase() {

    abstract fun preferenceDao(): PreferenceDao

    private class CrapchatDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            INSTANCE?.let { database ->
                scope.launch {
                    var preferenceDao = database.preferenceDao()
                    // Do any pre population here
                }
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: CrapchatRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): CrapchatRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CrapchatRoomDatabase::class.java,
                    "crapchat_database"
                ).addCallback(CrapchatDatabaseCallback(scope)).fallbackToDestructiveMigration().allowMainThreadQueries().build()
                INSTANCE = instance
                return instance
            }
        }
    }

}