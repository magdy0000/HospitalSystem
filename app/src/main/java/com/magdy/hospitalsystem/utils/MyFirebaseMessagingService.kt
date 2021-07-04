package com.magdy.hospitalsystem.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.media.RingtoneManager
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.magdy.hospitalsystem.MainActivity
import com.magdy.hospitalsystem.R

class MyFirebaseMessagingService : FirebaseMessagingService(){

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        if (remoteMessage.notification?.title == null ||remoteMessage.notification?.body == null )
            return
        sendNotification(
            remoteMessage.notification?.title!!, remoteMessage.notification?.body!!
        )
    }

    private fun sendNotification(title: String, messageBody: String) {
        val sb: Spannable = SpannableString(title)
        sb.setSpan(StyleSpan(Typeface.BOLD), 0, title.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        var intent: Intent ?= null
        if (true) {
            intent = Intent(this, MainActivity::class.java)
        }
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT
        )
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channelId = getString(R.string.default_notification_channel_id)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Notfication",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "test channel"
            channel.enableLights(true)
            channel.lightColor = Color.BLUE
            channel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
            notificationManager.createNotificationChannel(channel)
        }
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_employee)
            .setVibrate(longArrayOf(0, 1000, 500, 1000))
            .setStyle(
                NotificationCompat.InboxStyle()
                    .setBigContentTitle(sb)
                    .addLine(messageBody)
            ).setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)


        // Since android Oreo notification channel is needed.
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    override fun onNewToken(s: String) {
        super.onNewToken(s)
        Log.e("TAG", "onNewToken: ")
    }

}