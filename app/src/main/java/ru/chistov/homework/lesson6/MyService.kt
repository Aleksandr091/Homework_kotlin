package ru.chistov.homework.lesson6

import android.app.IntentService
import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import ru.chistov.homework.utils.KEY_BUNDLE_ACTIVITY_MESSAGE
import ru.chistov.homework.utils.KEY_BUNDLE_SERVICE_MESSAGE

class MyService(val name:String = ""):IntentService(name) {
    override fun onHandleIntent(p0: Intent?) {
        Log.d("@@@","work MyService")
        p0?.let {
            val extra = it.getStringExtra(KEY_BUNDLE_ACTIVITY_MESSAGE)
            Log.d("@@@","work MyService $extra")
            val message = Intent("myaction")
            message.putExtra(KEY_BUNDLE_SERVICE_MESSAGE,"привет активити")
            //sendBroadcast(message)
            LocalBroadcastManager.getInstance(this).sendBroadcast(message)

        }
    }
}