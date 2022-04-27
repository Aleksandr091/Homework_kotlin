package ru.chistov.homework.lesson6

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import ru.chistov.homework.utils.KEY_BUNDLE_SERVICE_MESSAGE

class MyBroadcastReceiver:BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        Log.d("@@@","MyBroadcastReceiver onReceive ${p1!!.action}")
        p1?.let {
            val extra=it.getStringExtra(KEY_BUNDLE_SERVICE_MESSAGE)
            Log.d("@@@","MyBroadcastReceiver onReceive $extra")
        }
    }
}