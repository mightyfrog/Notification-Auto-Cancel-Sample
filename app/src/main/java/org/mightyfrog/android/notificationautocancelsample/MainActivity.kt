package org.mightyfrog.android.notificationautocancelsample

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @author Shigehiro Soejima
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            postTestNotification()
        }

        if (!NotificationManagerCompat.getEnabledListenerPackages(this).contains(packageName)) {
            Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS).apply {
                startActivity(this)
            }
        }
    }

    private fun postTestNotification() {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(NotificationChannel("channel_id_2", "channel name 2", NotificationManager.IMPORTANCE_HIGH))
        }

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 1, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        NotificationCompat.Builder(this, "channel_id_2")
                .setContentTitle("Test notification")
                .setContentText("This is a test notification.")
                .setSmallIcon(R.drawable.ic_android)
                .setContentIntent(pendingIntent)
                .build()
                .apply {
                    manager.notify(2, this)
                }
    }
}
