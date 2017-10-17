package com.integratorsb2b.ug.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.integratorsb2b.ug.R
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions

class NotificationService : Service() {

    private var indexNumber: String? = null

    private lateinit var pusher: Pusher

    private val notificationId = 1


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        indexNumber = intent?.getStringExtra("indexNumber")

        if (indexNumber == null) {
            onDestroy()
            return super.onStartCommand(intent, flags, startId)
        }

        Log.i("NotificationService", "Success and continuing...")

        val options = PusherOptions()
        options.setCluster("eu")

        pusher = Pusher("23cad76055fb80ffa985", options)
        pusher.subscribe(indexNumber).bind("PaymentPending") { channelName, eventName, data ->
            createNotification(data)
        }

        return START_REDELIVER_INTENT
    }

    private fun createNotification(data: String) {
        Log.i("NS", "About to create...")

        if (indexNumber == null) return
        Log.i("NS", "Creating notification")

        createNotificationChannel()

        val notificationBuilder = NotificationCompat.Builder(this, indexNumber!!)
        notificationBuilder.setContentTitle("University of Ghana")
        notificationBuilder.setContentText("Notification received")
        notificationBuilder.setSmallIcon(R.drawable.ic_notification_icon)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(indexNumber,
                    "UG Payment Status",
                    NotificationManager.IMPORTANCE_HIGH)

            notificationChannel.enableLights(true)
            notificationChannel.enableVibration(true)

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        pusher.disconnect()
        super.onDestroy()
    }
}