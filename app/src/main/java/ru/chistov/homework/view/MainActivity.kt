package ru.chistov.homework.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.chistov.homework.R
import ru.chistov.homework.view.weatherList.WeatherListFragment


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, WeatherListFragment.newInstance()).commit()
        }

    }


}




