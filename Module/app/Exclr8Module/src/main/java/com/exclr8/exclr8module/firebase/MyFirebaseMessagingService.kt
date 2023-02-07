package com.exclr8.exclr8module.firebase

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.core.content.ContextCompat.getSystemService

class MyFirebaseMessagingService
    //: FirebaseMessagingService()
{

 /*   override fun onMessageReceived(message: RemoteMessage) {
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        lateinit var notificationChannel: NotificationChannel
        lateinit var builder: Notification.Builder
        val channelId = "i.apps.notifications"
        val description = "Test notification"

        *//*val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("url", url)*//*
        //val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel =
                NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(this, channelId)
            //Build Notification
        } else {
            builder = Notification.Builder(this)
            //Build Notification
        }
        notificationManager.notify(1234, builder.build())
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        sendRegistrationToServer()
    }

    private fun sendRegistrationToServer() {
        ///Api To Post Fcm Token
    }*/
}