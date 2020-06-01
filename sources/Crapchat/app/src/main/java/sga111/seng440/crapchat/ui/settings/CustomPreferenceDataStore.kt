package sga111.seng440.crapchat.ui.settings

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sga111.seng440.crapchat.room.CrapchatRoomDatabase
import sga111.seng440.crapchat.room.Preference
import sga111.seng440.crapchat.room.PreferenceRepository

class CustomPreferenceDataStore(context: Context, scope: CoroutineScope) : PreferenceDataStore() {

    private val repository: PreferenceRepository
    private val scope: CoroutineScope = scope

    init {
        val preferencesDao = CrapchatRoomDatabase.getDatabase(context, scope).preferenceDao()
        repository = PreferenceRepository(preferencesDao)
    }

    override fun putBoolean(key: String, value: Boolean) {
        repository.insert(Preference(key, value))
    }

    override fun getBoolean(key: String, defValue: Boolean): Boolean {
        var preferenceResult : Preference? = repository.retrieve(key)
        return preferenceResult?.value ?: defValue
    }



}