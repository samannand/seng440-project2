package sga111.seng440.crapchat.ui.settings

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import sga111.seng440.crapchat.R
import sga111.seng440.crapchat.Util
import sga111.seng440.crapchat.room.CrapchatRoomDatabase
import sga111.seng440.crapchat.room.PreferenceRepository

class PreferencesFragment : PreferenceFragmentCompat() {


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        val preferenceManager = preferenceManager
        val dataStore = CustomPreferenceDataStore(context!!, lifecycleScope)
        preferenceManager.preferenceDataStore = dataStore
        setPreferencesFromResource(R.xml.preferences_screen, rootKey)

        val notificationsPreference: SwitchPreferenceCompat? = findPreference("notifications")

        notificationsPreference!!.setOnPreferenceChangeListener { preference, newValue ->

            if (newValue as Boolean) {
                // Was set to true so attempt to immediately schedule notificaiton
                Log.d("CRAPCHAT", "Attempting to schedule notification")
                Util.scheduleNotification(context!!)
            } else {
                // Delete any previously scheduled notifications
                Util.removeScheduledNotifications(context!!)
            }

            true
        }
    }

}