package ru.chistov.homework.view

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import ru.chistov.homework.R
import ru.chistov.homework.lesson6.MyBroadcastReceiver
import ru.chistov.homework.lesson6.MyService
import ru.chistov.homework.utils.KEY_BUNDLE_ACTIVITY_MESSAGE
import ru.chistov.homework.view.weatherList.WeatherListFragment


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, WeatherListFragment.newInstance()).commit()
        }
        startService(Intent(this, MyService::class.java).apply {
            putExtra(KEY_BUNDLE_ACTIVITY_MESSAGE, "привет сервис")
        })
        val receiver = MyBroadcastReceiver()
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, IntentFilter("myaction"))
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(receiver, IntentFilter("android.intent.action.AIRPLANE_MODE"))
        //registerReceiver(receiver, IntentFilter("myaction"))

    }


}




