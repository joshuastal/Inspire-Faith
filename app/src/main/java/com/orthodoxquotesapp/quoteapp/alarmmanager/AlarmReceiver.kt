package com.orthodoxquotesapp.quoteapp.alarmmanager

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.orthodoxquotesapp.quoteapp.R
import java.util.Calendar

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        // Log to ensure this method is called
        Log.d("DailyNotification", "Notification triggered at: ${Calendar.getInstance().time}")


        context?.let {
            val notificationManager =
                it.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val runnerNotifier = QuoteNotifier(notificationManager, it)
            runnerNotifier.showNotification() // Show the notification

            // Schedule the next alarm
            val alarmManager = it.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val nextAlarmTime = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_YEAR, 1) // Move to the next day
                set(Calendar.HOUR_OF_DAY, 7) // Set the desired hour
                set(Calendar.MINUTE, 0)     // Set the desired minute
                set(Calendar.SECOND, 0)     // Set the desired second
            }

            val nextIntent = Intent(it, AlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                it,
                0,
                nextIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            alarmManager.setWindow(
                AlarmManager.RTC_WAKEUP,
                nextAlarmTime.timeInMillis,
                60000,  // time window (1 minute)
                pendingIntent
            )
            Log.d("DailyNotification", "Next alarm scheduled for: ${nextAlarmTime.time}")
        }
    }
}