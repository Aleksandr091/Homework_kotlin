package ru.chistov.homework.repository

import ru.chistov.homework.repository.dto.WeatherDTO

fun interface OnServerResponse {
    fun onResponse(weatherDTO: WeatherDTO)
}