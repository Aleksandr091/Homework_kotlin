package ru.chistov.homework.lesson11

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.icu.text.CaseMap
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ru.chistov.homework.R

class MyService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        if (!message.data.isNullOrEmpty()){
            val title = message.data[KEY_TITLE]
            val message = message.data[KEY_MESSAGE]
            if (!title.isNullOrEmpty()&&!message.isNullOrEmpty()){
                push(title,message)
            }
        }

    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    companion object {
        private const val NOTIFICATION_ID_LOW = 1
        private const val NOTIFICATION_ID_HIGH = 2
        private const val CHANNEL_ID_LOW = "channel_id_1"
        private const val CHANNEL_ID_HIGH = "channel_id_2"
        private const val KEY_TITLE = "myTitle"
        private const val KEY_MESSAGE = "myMessage"
    }

    private fun push(title: String,message: String){
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationBuilderLow = NotificationCompat.Builder(this,CHANNEL_ID_LOW).apply {
            setSmallIcon(R.drawable.ic_map_marker)
            setContentTitle(title)
            setContentTitle(message)
            priority = NotificationManager.IMPORTANCE_LOW
        }
        val notificationBuilderHIGH = NotificationCompat.Builder(this,CHANNEL_ID_HIGH).apply {
            setSmallIcon(R.drawable.ic_map_pin)
            setContentTitle(title)
            setContentTitle(message)
            priority = NotificationManager.IMPORTANCE_HIGH
        }

        notificationManager.notify(NOTIFICATION_ID_LOW,notificationBuilderLow.build()) // до 26 версии
        notificationManager.notify(NOTIFICATION_ID_HIGH,notificationBuilderHIGH.build())

        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val channelNameLow = "Name $CHANNEL_ID_LOW"
            val channelDescriptionLow = "Description $CHANNEL_ID_LOW"
            val channelPriorityLow = NotificationManager.IMPORTANCE_LOW
            val channelLow = NotificationChannel(CHANNEL_ID_LOW,channelNameLow,channelPriorityLow).apply {
                description = channelDescriptionLow
            }
            notificationManager.createNotificationChannel(channelLow)
        }
        notificationManager.notify(NOTIFICATION_ID_LOW,notificationBuilderLow.build())

        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val channelNameHigh = "Name $CHANNEL_ID_HIGH"
            val channelDescriptionHigh = "Description $CHANNEL_ID_HIGH"
            val channelPriorityHigh = NotificationManager.IMPORTANCE_HIGH
            val channelHigh = NotificationChannel(CHANNEL_ID_HIGH,channelNameHigh,channelPriorityHigh).apply {
                description = channelDescriptionHigh
            }
            notificationManager.createNotificationChannel(channelHigh)
        }
        notificationManager.notify(NOTIFICATION_ID_HIGH,notificationBuilderHIGH.build())



    }
}