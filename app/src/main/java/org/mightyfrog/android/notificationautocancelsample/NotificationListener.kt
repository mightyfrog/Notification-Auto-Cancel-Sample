package org.mightyfrog.android.notificationautocancelsample

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.support.v4.app.NotificationCompat
import android.widget.Toast

/**
 * @author Shigehiro Soejima
 */
class NotificationListener : NotificationListenerService() {

    override fun onBind(intent: Intent?): IBinder? {
        postNotification()

        return super.onBind(intent)
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        sbn?.let {
            if (it.packageName == packageName) {
                cancelNotification(it.key)
                Toast.makeText(this, "canceled", Toast.LENGTH_SHORT).show()
            }
        }

        super.onNotificationPosted(sbn)
    }

    private fun postNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(
                NotificationChannel(
                    "channel_id_1",
                    "channel name 1",
                    NotificationManager.IMPORTANCE_MIN
                )
            )
        }

        val notif = NotificationCompat.Builder(this, "channel_id_1")
            .setContentTitle("Notification listener service")
            .setContentText("Hello world!")
            .setSmallIcon(R.drawable.ic_android)
            .setOngoing(true)
            .setShowWhen(false)
            .setContentIntent(
                PendingIntent.getActivity(
                    this,
                    1,
                    Intent(),
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            )
            .setPriority(Notification.PRIORITY_MIN)
            .build()
        startForeground(1, notif)
    }
}
