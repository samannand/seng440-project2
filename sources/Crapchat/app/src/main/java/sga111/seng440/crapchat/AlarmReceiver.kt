package sga111.seng440.crapchat

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log

fun Bundle.toParamsString() = keySet().map { "$it -> ${get(it)}" }.joinToString("\n")

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        //Log.d("FOO", "Received message ${intent.action} with\n${intent.extras!!.toParamsString()}")

        val intent = Intent(context, MainActivity::class.java).run {
            PendingIntent.getActivity(context, 0, this, 0)
        }

        val notification = Notification.Builder(context, Notification.CATEGORY_REMINDER).run {
            setSmallIcon(R.mipmap.ic_launcher)
            setContentTitle(context.getString(R.string.daily_reminder_title))
            setContentText(context.getString(R.string.daily_reminder_text))
            setContentIntent(intent)
            setAutoCancel(true)
            build()
        }

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(0, notification)

    }
}