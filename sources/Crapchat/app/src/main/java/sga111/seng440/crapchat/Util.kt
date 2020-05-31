package sga111.seng440.crapchat

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import androidx.preference.PreferenceManager
import java.util.*

object Util {

    fun scheduleNotification(context: Context) {
        var intent = Intent(context, AlarmReceiver::class.java).let {
            PendingIntent.getBroadcast(context, 0, it, 0)
        }

        val today = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, MainActivity.NOTIFICATION_HOUR)
            set(Calendar.MINUTE, MainActivity.NOTIFICATION_MINUTE)
        }

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        //alarmManager.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 30000, intent)
        alarmManager.setInexactRepeating(AlarmManager.RTC, today.timeInMillis, AlarmManager.INTERVAL_DAY, intent)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor : SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean("notificationSet", true)
        editor.apply()

    }

    fun removeScheduledNotifications(context: Context) {
        var intent = Intent(context, AlarmReceiver::class.java).let {
            PendingIntent.getBroadcast(context, 0, it, 0)
        }

        Log.d("CRAPCHAT", "Cancelling intent")
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(intent)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor : SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean("notificationSet", false)
        editor.apply()
    }

}