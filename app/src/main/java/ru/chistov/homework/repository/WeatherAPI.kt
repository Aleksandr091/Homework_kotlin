package ru.chistov.homework.repository

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import ru.chistov.homework.repository.dto.WeatherDTO
import ru.chistov.homework.utils.API_KEY
import ru.chistov.homework.utils.KEY_BUNDLE_LAT
import ru.chistov.homework.utils.KEY_BUNDLE_LON
import ru.chistov.homework.utils.YANDEX_ENDPOINT

interface WeatherAPI {
    @GET(YANDEX_ENDPOINT)
    fun getWeather(
        @Header(API_KEY) apiKey: String,
        @Query(KEY_BUNDLE_LAT) lat: Double,
        @Query(KEY_BUNDLE_LON) lon: Double
    ): Call<WeatherDTO>
}