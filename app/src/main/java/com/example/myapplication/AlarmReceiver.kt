package com.example.myapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import io.karn.notify.Notify

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Notify
            .with(context!!)
            .content {
                this.title
                text = "I got triggered at - ass"
            }
            .show()
        Log.d("AlrmReceivar", "fired")
    }

}