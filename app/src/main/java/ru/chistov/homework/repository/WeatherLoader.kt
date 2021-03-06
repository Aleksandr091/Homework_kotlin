package ru.chistov.homework.repository


import android.os.Handler
import android.os.Looper
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import ru.chistov.homework.BuildConfig
import ru.chistov.homework.repository.dto.WeatherDTO
import ru.chistov.homework.utils.API_KEY
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class WeatherLoader(
    private val onServerResponseListener: OnServerResponse,
    private val onErrorListener: OnErrorListener
) {

    fun loadWeather(lat: Double, lon: Double) {
        val looper = Handler(Looper.getMainLooper())

        Thread {
            //val urlText = "${YANDEX_DOMAIN}${YANDEX_ENDPOINT}lat=$lat&lon=$lon"
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
                        looper.post { onErrorListener.onError("ошибка сервера " + urlConnection.responseMessage) }
                    }
                    in clientside -> {
                        looper.post { onErrorListener.onError("у вас какая-то ошибка " + urlConnection.responseMessage) }
                    }
                    in responseOk -> {
                        val buffer = BufferedReader(InputStreamReader(urlConnection.inputStream))
                        val weatherDTO: WeatherDTO = Gson().fromJson(buffer, WeatherDTO::class.java)
                        looper.post {
                            onServerResponseListener.onResponse(
                                weatherDTO
                            )
                        }
                    }
                }
            } catch (e: JsonSyntaxException) {
                looper.post { onErrorListener.onError(e.message.toString()) }
            } finally {
                urlConnection.disconnect()
            }

        }.start()
    }
}