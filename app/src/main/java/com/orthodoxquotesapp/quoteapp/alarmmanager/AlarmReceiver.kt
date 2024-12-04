package com.orthodoxquotesapp.quoteapp.alarmmanager

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.orthodoxquotesapp.quoteapp.R

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        // Log to ensure this method is called
        Log.d("DailyNotification", "Notification triggered at: ${System.currentTimeMillis()}")


        context?.let {
            val notificationManager =
                it.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val runnerNotifier = QuoteNotifier(notificationManager, it)
            runnerNotifier.showNotification() // Show the notification
        }
    }
}