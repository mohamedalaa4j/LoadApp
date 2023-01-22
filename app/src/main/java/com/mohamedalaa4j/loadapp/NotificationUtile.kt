package com.mohamedalaa4j.loadapp

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat

const val NOTIFICATION_ID = 0

fun NotificationManager.sendNotification(messageBody: String, context: Context) {

    var builder = NotificationCompat.Builder(context, DOWNLOAD_NOTIFICATION_CHANNEL)
        .setSmallIcon(R.drawable.download)
        .setContentTitle("LoadApp")
        .setContentText(messageBody)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    notify(NOTIFICATION_ID, builder.build())
}