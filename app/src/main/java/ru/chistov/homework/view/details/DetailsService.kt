package ru.chistov.homework.view.details

import android.app.IntentService
import android.content.Intent
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import ru.chistov.homework.BuildConfig
import ru.chistov.homework.repository.OnErrorListener
import ru.chistov.homework.repository.dto.WeatherDTO
import ru.chistov.homework.utils.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class DetailsService(val name: String = "") :
    IntentService(name) {
    private lateinit var onErrorListener: OnErrorListener
    override fun onHandleIntent(p0: Intent?) {
        p0?.let {
            val lat = it.getDoubleExtra(KEY_BUNDLE_LAT, 0.0)
            val lon = it.getDoubleExtra(KEY_BUNDLE_LON, 0.0)

            //val urlText = "${YANDEX_DOMAIN}${YANDEX_PATH}lat=$lat&lon=$lon"
            val urlText = "http://212.86.114.27/v2/informers?lat=$lat&lon=$lon"
            val uri = URL(urlText)
            val urlConnection: HttpURLConnection =
                (uri.openConnection() as HttpURLConnection).apply {
                    connectTimeout = 1000
                    readTimeout = 1000
                    addRequestProperty(API_KEY, BuildConfig.WEATHER_API_KEY)
                }
            try {
                val headers = urlConnection.headerFields
                val responseCode = urlConnection.responseCode
                val responseMessage = urlConnection.responseMessage
                val serverside = 500..599
                val clientside = 400..499
                val responseOk = 200..299

                when (responseCode) {
                    in serverside -> {
                        onErrorListener.onError("ошибка сервера " + urlConnection.responseMessage)
                    }
                    in clientside -> {
                        onErrorListener.onError("у вас какая-то ошибка " + urlConnection.responseMessage)
                    }
                    in responseOk -> {
                        val buffer = BufferedReader(InputStreamReader(urlConnection.inputStream))
                        val weatherDTO: WeatherDTO = Gson().fromJson(buffer, WeatherDTO::class.java)
                        val message = Intent(KEY_WAVE)
                        message.putExtra(KEY_BUNDLE_SERVICE_BROADCAST_WEATHER, weatherDTO)
                        sendBroadcast(message)
                    }
                }
            } catch (e: JsonSyntaxException) {
                onErrorListener.onError(urlConnection.responseMessage)
            } finally {
                urlConnection.disconnect()
            }


        }
    }
}