package com.mohamedalaa4j.loadapp

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

const val NOTIFICATION_ID = 0

fun NotificationManager.sendNotification(messageBody: String, context: Context, filename:String, status:String) {

    val intent = Intent(context, DetailActivity::class.java)
    intent.putExtra("FILE_NAME_KEY",filename)
    intent.putExtra("STATUS_KEY",status)

    val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT else 0

    val pendingIntent = PendingIntent.getActivity(context, NOTIFICATION_ID, intent, flag)


    val builder = NotificationCompat.Builder(context, DOWNLOAD_NOTIFICATION_CHANNEL)
        .setSmallIcon(R.drawable.download)
        .setContentTitle(context.getString(R.string.app_name))
        .setContentText(messageBody)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)

        //Notification listener
        .setContentIntent(pendingIntent)

            //Notification button listener
        .addAction(R.drawable.download, "Details", pendingIntent)

    notify(NOTIFICATION_ID, builder.build())
}