package com.orthodoxquotesapp.quoteapp.alarmmanager

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.orthodoxquotesapp.quoteapp.R

class QuoteNotifier(
    private val notificationManager: NotificationManager,
    private val context: Context
) {

    private val notificationChannelId: String = "quote_channel"
    private val notificationChannelName: String = "Quotes"
    private val notificationId: Int = 200

    // Create and show the notification
    @SuppressLint("ObsoleteSdkInt")
    fun showNotification() {
        Log.d("DailyNotification", "Showing notification at: ${System.currentTimeMillis()}")

        // Create notification channel for devices with Android Oreo (API 26) or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                notificationChannelId,
                notificationChannelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Build the notification
        val notification = NotificationCompat.Builder(context, notificationChannelId)
            .setSmallIcon(R.drawable.orthodox_icon) // Your app's icon
            .setContentTitle("Orthodox Quotes")
            .setContentText("Your daily Orthodox Quotes are ready!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        // Display the notification
        notificationManager.notify(notificationId, notification)
    }
}