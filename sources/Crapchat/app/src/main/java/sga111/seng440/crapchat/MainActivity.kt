package sga111.seng440.crapchat

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import sga111.seng440.crapchat.ui.settings.PreferencesActivity
import java.util.*

class MainActivity : AppCompatActivity() {

    private val alarmReceiver = AlarmReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_chats, R.id.navigation_camera, R.id.navigation_map))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        createNotificationChannel()

        // TODO: Room updates

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val notificationsEnabled = sharedPreferences.getBoolean("notifications", false)
        val setNotification = sharedPreferences.getBoolean("notificationSet", false)
        if (notificationsEnabled && !setNotification) {
            Util.scheduleNotification(applicationContext)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.action_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settings_button -> {
                showPreferencesScreen()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun showPreferencesScreen() {
        //Toast.makeText(this, "Coming soon!", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, PreferencesActivity::class.java)
        startActivity(intent)

    }

    private fun createNotificationChannel() {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(Notification.CATEGORY_REMINDER, "Reminder to Snap!", importance).apply {
            description = "Send daily reminders to check out on friends using Crapchat"
        }
        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        const val NOTIFICATION_HOUR = 0
        const val NOTIFICATION_MINUTE = 0
    }
}
