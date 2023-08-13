package com.example.foregroundtests

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.foregroundtest.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class TestService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notificationManager = getSystemService(NotificationManager::class.java)

        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            "My Channel Name",
            NotificationManager.IMPORTANCE_LOW
        )

        Log.d(javaClass.simpleName, "Notifications enabled ${notificationManager.areNotificationsEnabled()}")

        notificationManager.createNotificationChannel(channel)

        val pendingIntent: PendingIntent = Intent(this, MainActivity::class.java)
            .apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
            .let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)
            }

        val notification: Notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setContentTitle("Example App")
            .setContentText("Notification Content")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(pendingIntent)
            .setForegroundServiceBehavior(Notification.FOREGROUND_SERVICE_IMMEDIATE)
            .build()


        runBlocking {
            Log.d(this@TestService.javaClass.simpleName, "Starting ${this@TestService.javaClass.simpleName} in 4 seconds")
            delay(4000)
        }

        notificationManager.notify(ONGOING_NOTIFICATION_ID, notification)
        startForeground(ONGOING_NOTIFICATION_ID, notification)

        Log.d(this@TestService.javaClass.simpleName, "Started ${this@TestService.javaClass.simpleName}")

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(javaClass.simpleName, "Destroyed")
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    companion object {
        const val ONGOING_NOTIFICATION_ID = 22
        const val NOTIFICATION_CHANNEL_ID = "my_channel_id"
    }
}