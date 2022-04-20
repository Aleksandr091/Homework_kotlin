package ru.chistov.homework.repository

fun interface OnServerResponse {
    fun onResponse(weatherDTO: WeatherDTO)
}