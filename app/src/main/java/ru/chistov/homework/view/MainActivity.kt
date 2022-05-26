package ru.chistov.homework.view

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import ru.chistov.homework.R
import ru.chistov.homework.lesson10.MapsFragment
import ru.chistov.homework.lesson6.MyBroadcastReceiver
import ru.chistov.homework.lesson6.MyService
import ru.chistov.homework.lesson9.WorkWithContentProviderFragment
import ru.chistov.homework.utils.KEY_BUNDLE_ACTIVITY_MESSAGE
import ru.chistov.homework.view.historyList.HistoryWeatherListFragment
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
        registerReceiver(receiver, IntentFilter("android.intent.action.AIRPLANE_MODE"))
        //registerReceiver(receiver, IntentFilter("myaction"))

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("mylogs_push", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            val token = task.result
            Log.d("mylogs_push", "$token")
        })

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_history -> {
                val fragmentA = supportFragmentManager.findFragmentByTag("History")
                if (fragmentA == null) {
                    supportFragmentManager.beginTransaction().apply {
                        add(R.id.container, HistoryWeatherListFragment.newInstance(), "History")
                            .addToBackStack("").commit()
                    }
                }
            }
            R.id.action_work_with_content_provider -> {
                val fragmentB = supportFragmentManager.findFragmentByTag("WW")
                if (fragmentB == null) {
                    supportFragmentManager.beginTransaction().apply {
                        add(R.id.container, WorkWithContentProviderFragment.newInstance(), "WW")
                            .addToBackStack("").commit()
                    }
                }
            }
            R.id.action_menu_google_maps -> {
                val fragmentC = supportFragmentManager.findFragmentByTag("GM")
                if (fragmentC == null) {
                    supportFragmentManager.beginTransaction().apply {
                        add(R.id.container, MapsFragment(), "GM")
                            .addToBackStack("").commit()
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}




