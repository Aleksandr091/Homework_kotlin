package ru.chistov.homework.repository

import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.chistov.homework.BuildConfig
import ru.chistov.homework.repository.dto.WeatherDTO
import ru.chistov.homework.utils.YANDEX_DOMAIN_HARD_MODE
import ru.chistov.homework.utils.convertDtoToModel
import ru.chistov.homework.viewmodel.DetailsViewModel

class DetailsRepositoryRetrofit2Impl : DetailsRepository {
    override fun getWeatherDetails(city: City, myCallback: DetailsViewModel.MyCallback) {

        val weatherAPI = Retrofit.Builder().apply {
            baseUrl(YANDEX_DOMAIN_HARD_MODE)
            addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        }.build().create(WeatherAPI::class.java)

        weatherAPI.getWeather(BuildConfig.WEATHER_API_KEY, city.lat, city.lon)
            .enqueue(object : Callback<WeatherDTO> {
                override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            val weather = convertDtoToModel(it)
                            weather.city = city
                            myCallback.onResponse(weather)
                        }
                    } else {
                        myCallback.onFailure("${response.message()}${response.code()}")
                    }
                }

                override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {

                    myCallback.onFailure("Что-то пошло не так" + t.message)
                }

            })


    }
}