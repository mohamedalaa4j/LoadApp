package com.mohamedalaa4j.loadapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class MyReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {

        val message = intent?.getStringExtra("MESSAGE")

        if (message != null) {

          //  context.applicationContext.startActivity(Intent(context.applicationContext, DetailActivity::class.java))
        }
    }
}