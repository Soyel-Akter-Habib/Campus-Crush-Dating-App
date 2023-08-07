package com.example.sltchat.utilities

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.sltchat.MainActivity
import com.example.sltchat.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

class MyFirebaseMessagingService :FirebaseMessagingService() {

    private val chanId = "onlinedates"

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val inten = Intent(this,MainActivity::class.java)
        inten.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val manager = getSystemService(Context.NOTIFICATION_SERVICE)
        createNotificationChannel(manager as NotificationManager)
        val inten1 = PendingIntent.getActivities(this,0, arrayOf(inten),PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this,chanId)
            .setContentTitle(message.data["title"])
            .setContentText(message.data["message"])
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setAutoCancel(true)
            .setContentIntent(inten1)
            .build()

        manager.notify(Random.nextInt(),notification)

    }
    private fun createNotificationChannel(manager: NotificationManager){
        val channel = NotificationChannel(chanId,"datingchats",NotificationManager.IMPORTANCE_HIGH)

        channel.description = "New Chat"
        channel.enableLights(true)

        manager.createNotificationChannel(channel)

    }
}