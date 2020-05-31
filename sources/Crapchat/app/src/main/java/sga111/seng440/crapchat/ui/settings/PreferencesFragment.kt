package sga111.seng440.crapchat.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import sga111.seng440.crapchat.R

class PreferencesFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences_screen, rootKey)
    }
}