package ru.chistov.homework.view.details

import android.app.IntentService
import android.content.Intent
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import ru.chistov.homework.BuildConfig
import ru.chistov.homework.repository.dto.WeatherDTO
import ru.chistov.homework.utils.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class DetailsService(val name: String = "") :
    IntentService(name) {
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
                        val errorSS = "ошибка сервера " + urlConnection.responseMessage
                        val messageErrorServerside = Intent(KEY_WAVE)
                        messageErrorServerside.putExtra(KEY_MESSAGE_ERROR_SERVERSIDE, errorSS)
                        sendBroadcast(messageErrorServerside)

                    }
                    in clientside -> {
                        val errorCS = "у вас какая-то ошибка " + urlConnection.responseMessage
                        val messageErrorClientside = Intent(KEY_WAVE)
                        messageErrorClientside.putExtra(KEY_MESSAGE_ERROR_CLIENTSIDE, errorCS)
                        sendBroadcast(messageErrorClientside)

                    }
                    in responseOk -> {
                        val buffer = BufferedReader(InputStreamReader(urlConnection.inputStream))
                        val weatherDTO: WeatherDTO = Gson().fromJson(buffer, WeatherDTO::class.java)
                        val message = Intent(KEY_WAVE)
                        message.putExtra(KEY_BUNDLE_SERVICE_BROADCAST_WEATHER, weatherDTO)
                        sendBroadcast(message)
                    }
                    else -> {}
                }
            } catch (e: JsonSyntaxException) {
                val error = urlConnection.responseMessage
                val messageError = Intent(KEY_WAVE)
                messageError.putExtra(KEY_MESSAGE_ERROR, error)
                sendBroadcast(messageError)
            } finally {
                urlConnection.disconnect()
            }


        }
    }


}