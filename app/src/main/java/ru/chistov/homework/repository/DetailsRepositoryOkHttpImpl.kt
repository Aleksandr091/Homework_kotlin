package ru.chistov.homework.repository

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import ru.chistov.homework.BuildConfig
import ru.chistov.homework.repository.dto.WeatherDTO
import ru.chistov.homework.utils.API_KEY
import ru.chistov.homework.utils.YANDEX_DOMAIN_HARD_MODE
import ru.chistov.homework.utils.YANDEX_ENDPOINT
import ru.chistov.homework.utils.convertDtoToModel
import ru.chistov.homework.viewmodel.DetailsViewModel

class DetailsRepositoryOkHttpImpl : DetailsRepository {
    override fun getWeatherDetails(city: City, myCallback: DetailsViewModel.MyCallback) {
        val client = OkHttpClient()
        val builder = Request.Builder()
        builder.addHeader(API_KEY, BuildConfig.WEATHER_API_KEY)
        builder.url("$YANDEX_DOMAIN_HARD_MODE${YANDEX_ENDPOINT}lat=${city.lat}&lon=${city.lon}")
        val request = builder.build()
        val call = client.newCall(request)
        Thread {
            val response = call.execute()
            if (response.isSuccessful) {
                val serverResponse = response.body()!!.string()
                val weatherDTO: WeatherDTO = Gson().fromJson(serverResponse, WeatherDTO::class.java)
                val weather = convertDtoToModel(weatherDTO)
                weather.city = city
                myCallback.onResponse(weather)

            } else {
                myCallback.onFailure("${response.message()}${response.code()}")
            }
        }.start()
    }
}